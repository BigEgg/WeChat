package com.thoughtworks.wechat_application.configs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Singleton;
import org.hibernate.validator.constraints.NotEmpty;

@Singleton
public class OAuthConfiguration {
    @NotEmpty
    private int oAuthAccessTokenExpireSeconds;

    @NotEmpty
    private int oAuthRefreshTokenExpireSeconds;

    @JsonProperty("access_token_expire_seconds")
    public int getoAuthAccessTokenExpireSeconds() {
        return oAuthAccessTokenExpireSeconds;
    }

    @JsonProperty("refresh_token_expire_seconds")
    public int getoAuthRefreshTokenExpireSeconds() {
        return oAuthRefreshTokenExpireSeconds;
    }
}
