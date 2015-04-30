package com.thoughtworks.wechat_application.logic;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.configs.OAuthConfiguration;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import com.thoughtworks.wechat_application.models.oauth.OAuthInfo;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

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

    public OAuthInfo newOAuth(final OAuthClient client) {
        checkNotNull(client);
        cleanUp();

        deleteExistOAuth(client);
        LOGGER.info("[NewOAuth] Try delete exist OAuth based on the authenticate information.");

        final String accessToken = getAccessToken();
        final String refreshToken = getRefreshToken();
        final OAuthInfo oAuthInfo = new OAuthInfo(
                accessToken,
                refreshToken,
                client,
                oAuthConfiguration.getoAuthAccessTokenExpireSeconds(),
                oAuthConfiguration.getoAuthRefreshTokenExpireSeconds(),
                DateTime.now());
        oAuthInfoMap.put(accessToken, oAuthInfo);
        LOGGER.info("[NewOAuth] Generate new OAuth tokens for client: {}, access token: {}, refresh token: {}.", client.getClientId(), accessToken, refreshToken);
        return oAuthInfo;
    }

    public Optional<OAuthInfo> refreshAccessToken(final String accessToken, final String refreshToken) {
        cleanUp();

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

    public void cleanUp() {
        LOGGER.info("[CleanUp] Try clear all expired OAuth information.");
        for (Map.Entry<String, OAuthInfo> entry : oAuthInfoMap.entrySet()) {
            if (!entry.getValue().getRefreshToken().isPresent()) {
                oAuthInfoMap.remove(entry.getKey());
            }
        }
    }

    public Optional<OAuthClient> getOAuthClient(final String accessToken) {
        checkNotBlank(accessToken);
        cleanUp();

        LOGGER.info("[GetOAuthClient] Try get OAuth Client by access token: {}.", accessToken);
        if (oAuthInfoMap.containsKey(accessToken)) {
            final OAuthClient client = oAuthInfoMap.get(accessToken).getClient();
            LOGGER.info("[GetOAuthClient] Find the OAuth Client(id: {}).", client.getId());
            return Optional.of(client);
        } else {
            LOGGER.info("[GetOAuthClient] Cannot find the access token: {}.", accessToken);
            return Optional.empty();
        }
    }

    public Optional<OAuthClient> removeOAuthInfo(final String accessToken) {
        checkNotBlank(accessToken);
        cleanUp();

        LOGGER.info("[RemoveOAuthClient] Try remove OAuth Info by access token: {}.", accessToken);
        if (oAuthInfoMap.containsKey(accessToken)) {
            final OAuthClient client = oAuthInfoMap.remove(accessToken).getClient();
            LOGGER.info("[RemoveOAuthClient] Find the OAuth Info with Client(id: {}), delete it", client.getId());
            return Optional.of(client);
        } else {
            LOGGER.info("[RemoveOAuthClient] Cannot find the access token: {}.", accessToken);
            return Optional.empty();
        }
    }

    private void deleteExistOAuth(final OAuthClient client) {
        final Optional<Map.Entry<String, OAuthInfo>> existOAuth = oAuthInfoMap.entrySet().stream().filter(entry -> {
            final OAuthInfo value = entry.getValue();
            return Objects.equals(value.getClient().getClientId(), client.getClientId());
        }).findFirst();
        if (existOAuth.isPresent()) {
            LOGGER.info("[DeleteExistOAuth] Find the exist OAuth information, delete it.");
            oAuthInfoMap.remove(existOAuth.get().getKey());
        }
    }

    private String getAccessToken() {
        String accessToken = getToken();
        while (oAuthInfoMap.containsKey(accessToken)) {
            LOGGER.warn("[GetAccessToken] Generate new access token: {}, but it already exist. Re-generate.", accessToken);
            accessToken = getToken();
        }
        return accessToken;
    }

    private String getRefreshToken() {
        return getToken();
    }

    private String getToken() {
        final String uuid = UUID.randomUUID().toString();
        final String[] values = uuid.split("-");

        return values[0];
    }
}
