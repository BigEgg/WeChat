package com.thoughtworks.wechat_application.models.oauth;

import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import org.joda.time.DateTime;

import java.util.Optional;

public class OAuthInfo {
    private final String refreshToken;
    private final DateTime refreshTokenExpireTime;
    private final OAuthClient client;
    private String accessToken;
    private DateTime accessTokenExpireTime;

    public OAuthInfo(final String accessToken,
                     final String refreshToken,
                     final OAuthClient client,
                     final int accessTokenExpireInSeconds,
                     final int refreshTokenExpireInSeconds,
                     final DateTime createTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.client = client;
        this.accessTokenExpireTime = createTime.plusSeconds(accessTokenExpireInSeconds);
        this.refreshTokenExpireTime = createTime.plusSeconds(refreshTokenExpireInSeconds);
    }

    public Optional<String> getAccessToken() {
        return accessTokenExpireTime.isAfterNow() && refreshTokenExpireTime.isAfterNow()
                ? Optional.of(accessToken)
                : Optional.<String>empty();
    }

    public Optional<String> getRefreshToken() {
        return refreshTokenExpireTime.isAfterNow()
                ? Optional.of(refreshToken)
                : Optional.<String>empty();
    }

    public OAuthClient getClient() {
        return client;
    }

    public void updateAccessToken(final String accessToken, final int accessTokenExpireInSeconds) {
        this.accessToken = accessToken;
        this.accessTokenExpireTime = DateTime.now().plusSeconds(accessTokenExpireInSeconds);
    }
}
