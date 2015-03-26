package com.thoughtworks.wechat_application.api.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminLoginResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("name")
    private String name;

    public AdminLoginResponse() {
    }

    public AdminLoginResponse(final String accessToken, final String refreshToken, String name) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getName() {
        return name;
    }
}
