package com.thoughtworks.wechat_application.models.oauth;

import org.joda.time.DateTime;

import java.util.Optional;

public class OAuthInfo {
    private final AuthenticateRole role;
    private final String refreshToken;
    private final Object client;
    private final DateTime refreshTokenExpireTime;
    private String accessToken;
    private DateTime accessTokenExpireTime;

    public OAuthInfo(final AuthenticateRole role,
                     final String accessToken,
                     final String refreshToken,
                     final Object client,
                     final int accessTokenExpireInSeconds,
                     final int refreshTokenExpireInSeconds,
                     final DateTime createTime) {
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.client = client;
        this.accessTokenExpireTime = createTime.plusSeconds(accessTokenExpireInSeconds);
        this.refreshTokenExpireTime = createTime.plusSeconds(refreshTokenExpireInSeconds);
    }

    public AuthenticateRole getRole() {
        return role;
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

    public Object getClient() {
        return client;
    }

    public void updateAccessToken(final String accessToken, final int accessTokenExpireInSeconds) {
        this.accessToken = accessToken;
        this.accessTokenExpireTime = DateTime.now().plusSeconds(accessTokenExpireInSeconds);
    }
}
