package com.thoughtworks.wechat_application.api.admin.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeChatConnectionStatusResponse {
    private boolean weChatConnectedStatus;
    private boolean weChatAPIStatus;

    public WeChatConnectionStatusResponse() {
    }

    public WeChatConnectionStatusResponse(final boolean weChatConnectedStatus, final boolean weChatAPIStatus) {
        this.weChatConnectedStatus = weChatConnectedStatus;
        this.weChatAPIStatus = weChatAPIStatus;
    }

    @JsonProperty("server_connected")
    public boolean getWeChatConnectedStatus() {
        return weChatConnectedStatus;
    }

    @JsonProperty("api_status")
    public boolean getWeChatAPIStatus() {
        return weChatAPIStatus;
    }
}
