package com.thoughtworks.wechat_application.api.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthResponse {
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("access_token")
    private String accessToken;

    public OAuthResponse() {
    }

    public OAuthResponse(final String refreshToken, final String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
