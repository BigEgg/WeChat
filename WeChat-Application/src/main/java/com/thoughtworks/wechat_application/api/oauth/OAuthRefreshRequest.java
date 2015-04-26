package com.thoughtworks.wechat_application.api.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

public class OAuthRefreshRequest {
    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;
    @NotBlank
    @JsonProperty("access_token")
    private String accessToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
