package com.thoughtworks.wechat_application.api.admin.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeveloperInfoResponse {
    @JsonProperty("app_id")
    private String appId;
    @JsonProperty("app_secret")
    private String appSecret;

    public DeveloperInfoResponse() {
    }

    public DeveloperInfoResponse(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }
}
