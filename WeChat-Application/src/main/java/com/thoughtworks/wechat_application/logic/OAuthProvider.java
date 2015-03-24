package com.thoughtworks.wechat_application.logic;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.configs.OAuthConfiguration;
import com.thoughtworks.wechat_application.models.oauth.AuthenticateRole;
import com.thoughtworks.wechat_application.models.oauth.OAuthInfo;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.thoughtworks.wechat_core.util.HashHelper.sha1Hash;

@Singleton
public class OAuthProvider {
    private final static Logger LOGGER = LoggerFactory.getLogger(OAuthProvider.class);
    private final Map<String, OAuthInfo> oAuthInfoMap;
    private final OAuthConfiguration oAuthConfiguration;

    @Inject
    public OAuthProvider(final OAuthConfiguration oAuthConfiguration) {
        this.oAuthConfiguration = oAuthConfiguration;

        this.oAuthInfoMap = new HashMap<>();
    }

    public OAuthInfo newOAuth(final AuthenticateRole role) {
        String accessToken = getAccessToken();

        final String refreshToken = getToken(oAuthConfiguration.getoAuthRefreshTokenLength());
        final OAuthInfo oAuthInfo = new OAuthInfo(
                role,
                accessToken,
                refreshToken,
                oAuthConfiguration.getoAuthAccessTokenExpireSeconds(),
                oAuthConfiguration.getoAuthRefreshTokenExpireSeconds(),
                DateTime.now());
        oAuthInfoMap.put(accessToken, oAuthInfo);
        LOGGER.info("[NewOAuth] Generate new OAuth tokens for role: {}, access token: {}, refresh token: {}.", role, accessToken, refreshToken);
        return oAuthInfo;
    }

    public Optional<OAuthInfo> refreshAccessToken(final String accessToken, final String refreshToken) {
        LOGGER.info("[RefreshAccessToken] Try refresh access token with current access token: {}, and refresh token: {}.", accessToken, refreshToken);
        if (oAuthInfoMap.containsKey(accessToken)) {
            final OAuthInfo oAuthInfo = oAuthInfoMap.get(accessToken);
            if (oAuthInfo.getRefreshToken().isPresent() && oAuthInfo.getRefreshToken().get().equals(refreshToken)) {
                final String newAccessToken = getAccessToken();
                LOGGER.info("[RefreshAccessToken] Refresh token: '{}' still valid, give a new access token: {}.", refreshToken, newAccessToken);
                oAuthInfo.updateAccessToken(newAccessToken, oAuthConfiguration.getoAuthAccessTokenExpireSeconds());
                oAuthInfoMap.remove(accessToken);
                oAuthInfoMap.put(newAccessToken, oAuthInfo);
                return Optional.of(oAuthInfo);
            } else {
                LOGGER.info("[RefreshAccessToken] Refresh token: '{}' had expired or not valid. Cannot get OAuth information.", refreshToken);
                return Optional.empty();
            }
        } else {
            LOGGER.info("[RefreshAccessToken] Cannot find the access token: '{}'.", accessToken);
            return Optional.empty();
        }
    }

    public AuthenticateRole getRole(final String accessToken) {
        LOGGER.info("[GetRole] Try to get authenticate role with OAuth access token: {}.", accessToken);
        if (oAuthInfoMap.containsKey(accessToken)) {
            final OAuthInfo oAuthInfo = oAuthInfoMap.get(accessToken);
            final boolean expired = !oAuthInfo.getAccessToken().isPresent();
            LOGGER.info("[GetRole] Check access token: '{}' status, expired? {}.", accessToken, expired);
            return expired
                    ? AuthenticateRole.NONE
                    : oAuthInfo.getRole();
        } else {
            LOGGER.info("[GetRole] Cannot find the access token: '{}'.", accessToken);
            return AuthenticateRole.NONE;
        }
    }

    public void cleanUp() {
        oAuthInfoMap.entrySet().stream()
                .filter(entry -> !entry.getValue().getRefreshToken().isPresent())
                .forEach(entry -> oAuthInfoMap.remove(entry.getKey()));
    }

    private String getAccessToken() {
        String accessToken = getToken(oAuthConfiguration.getoAuthAccessTokenLength());
        while (oAuthInfoMap.containsKey(accessToken)) {
            LOGGER.warn("[GetAccessToken] Generate new access token: {}, but it already exist. Re-generate.", accessToken);
            accessToken = getToken(oAuthConfiguration.getoAuthAccessTokenLength());
        }
        return accessToken;
    }

    private String getToken(final int length) {
        final String uuid = UUID.randomUUID().toString();
        String fullToken;
        try {
            fullToken = sha1Hash(uuid);
        } catch (final NoSuchAlgorithmException ex) {
            LOGGER.error("[GetToken] Cannot use SHA-1 algorithm to get the token.");
            fullToken = uuid;
        }
        return fullToken.substring(0, length);
    }
}
