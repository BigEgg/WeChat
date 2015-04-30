package com.thoughtworks.wechat_application.api.admin.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeveloperInfoResponse {
    private String appId;
    private String appSecret;

    public DeveloperInfoResponse() {
    }

    public DeveloperInfoResponse(final String appId, final String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    @JsonProperty("app_id")
    public String getAppId() {
        return appId;
    }

    @JsonProperty("app_secret")
    public String getAppSecret() {
        return appSecret;
    }
}
