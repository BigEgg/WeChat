package com.thoughtworks.wechat_application.api.admin.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerInfoResponse {
    private String entryPoint;
    private String appToken;

    public ServerInfoResponse() {
    }

    public ServerInfoResponse(final String entryPoint, final String appToken) {
        this.entryPoint = entryPoint;
        this.appToken = appToken;
    }

    @JsonProperty("entry_point")
    public String getEntryPoint() {
        return entryPoint;
    }

    @JsonProperty("token")
    public String getAppToken() {
        return appToken;
    }
}
