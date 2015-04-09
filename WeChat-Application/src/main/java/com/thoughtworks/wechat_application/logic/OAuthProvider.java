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
        LOGGER.info("[CleanUp] Clear all expired OAuth information.");
        oAuthInfoMap.entrySet().stream()
                .filter(entry -> !entry.getValue().getRefreshToken().isPresent())
                .forEach(entry -> oAuthInfoMap.remove(entry.getKey()));
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
