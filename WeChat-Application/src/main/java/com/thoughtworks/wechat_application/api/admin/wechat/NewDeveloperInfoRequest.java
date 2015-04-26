package com.thoughtworks.wechat_application.api.admin.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class NewDeveloperInfoRequest {
    @NotBlank
    @Length(max = 128, min = 5)
    @JsonProperty("app_id")
    private String appId;
    @NotBlank
    @Length(max = 128)
    @JsonProperty("app_secret")
    private String appSecret;

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }
}
