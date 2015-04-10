package com.thoughtworks.wechat_application.api.admin.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerInfoResponse {
    @JsonProperty("entry_point")
    private String entryPoint;
    @JsonProperty("token")
    private String appToken;

    public ServerInfoResponse() {
    }

    public ServerInfoResponse(String entryPoint, String appToken) {
        this.entryPoint = entryPoint;
        this.appToken = appToken;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public String getAppToken() {
        return appToken;
    }
}
