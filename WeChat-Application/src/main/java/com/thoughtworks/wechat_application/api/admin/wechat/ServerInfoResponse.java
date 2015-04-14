package com.thoughtworks.wechat_application.api.admin.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerInfoResponse {
    @JsonProperty("entry_point")
    private String entryPoint;
    @JsonProperty("token")
    private String appToken;
    @JsonProperty("connected")
    private boolean isConnected;

    public ServerInfoResponse() {
    }

    public ServerInfoResponse(String entryPoint, String appToken, boolean isConnected) {
        this.entryPoint = entryPoint;
        this.appToken = appToken;
        this.isConnected = isConnected;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public String getAppToken() {
        return appToken;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
