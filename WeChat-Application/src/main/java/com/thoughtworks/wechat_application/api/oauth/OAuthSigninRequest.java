package com.thoughtworks.wechat_application.api.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class OAuthSignInRequest {
    @NotBlank
    @Length(max = 32)
    @JsonProperty("clientId")
    private String clientId;
    @NotBlank
    @Length(max = 64)
    @JsonProperty("clientSecret")
    private String clientSecret;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
