package com.thoughtworks.wechat_application.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.jdbi.OAuthClientDAO;
import com.thoughtworks.wechat_application.jdbi.core.AuthenticateRole;
import com.thoughtworks.wechat_application.jdbi.core.Member;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import com.thoughtworks.wechat_application.services.admin.passwordUtils.PasswordHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

@Singleton
public class OAuthClientService {
    private final static Logger LOGGER = LoggerFactory.getLogger(OAuthClientService.class);
    private OAuthClientDAO OAuthClientDAO;
    private PasswordHelper passwordHelper;

    @Inject
    public OAuthClientService(final OAuthClientDAO OAuthClientDAO, final PasswordHelper passwordHelper) {
        this.OAuthClientDAO = OAuthClientDAO;
        this.passwordHelper = passwordHelper;
    }

    public Optional<OAuthClient> SignIn(final String clientId, final String clientSecret) {
        checkNotBlank(clientId);
        checkNotBlank(clientSecret);
        checkArgument(clientId.length() <= 32);

        final OAuthClient oAuthClient = OAuthClientDAO.getByClientId(clientId);
        if (oAuthClient != null) {
            String salt = passwordHelper.getSaltFromHashedPassword(oAuthClient.getHashedClientSecret());
            String hashedPassword = passwordHelper.saltHash(clientSecret, salt);
            if (oAuthClient.getHashedClientSecret().equals(hashedPassword)) {
                LOGGER.info("[SignIn] Log in OAuthClient with clientId: {}, and clientSecret: {}, success.", clientId, hashedPassword);
                return Optional.of(oAuthClient);
            } else {
                LOGGER.info("[SignIn] Log in OAuthClient with clientId: {}, and clientSecret: {}, failed. Password not right.", clientId, hashedPassword);
                return Optional.empty();
            }
        } else {
            LOGGER.info("[SignIn] Log in OAuthClient with clientId: {}, failed. No such user.", clientId);
            return Optional.empty();
        }
    }

    public Optional<OAuthClient> createAdmin(final String clientId, final String clientSecret) {
        checkNotBlank(clientId);
        checkNotBlank(clientSecret);
        checkArgument(clientId.length() <= 32);

        OAuthClient currentOAuthClient = OAuthClientDAO.getByClientId(clientId);
        if (currentOAuthClient == null) {
            String hashedPassword = passwordHelper.saltHash(clientSecret);
            long adminId = OAuthClientDAO.create(clientId, hashedPassword, AuthenticateRole.ADMIN);
            LOGGER.info("[CreateAdmin] Create a new admin user(id: {}) with clientId: {}, clientSecret: {}, role: 'ADMIN'.", adminId, clientId, hashedPassword);
            return Optional.of(OAuthClientDAO.getByClientId(clientId));
        } else {
            LOGGER.info("[CreateAdmin] Create a new admin user with clientId: {} failed. Already have an OAuthClient(id: {}) with same clientId", clientId, currentOAuthClient.getId());
            return Optional.empty();
        }
    }

    public void setMember(final OAuthClient OAuthClient, final Member member) {
        checkNotNull(OAuthClient);
        checkNotNull(member);

        OAuthClientDAO.setMember(OAuthClient.getClientId(), member.getId());
        LOGGER.info("[SetMember] Set member(id: {}) to OAuthClient(clientId: {}) success.", member.getId(), OAuthClient.getClientId());
    }
}
