package com.thoughtworks.wechat_core.messages.inbound;

public abstract class InboundMessage {
    private String toUser;
    private String fromUser;
    private int createdTime;
    private String messageType;

    public String getToUser() {
        return toUser;
    }

    public String getFromUser() {
        return fromUser;
    }

    public int getCreatedTime() {
        return createdTime;
    }

    public String getMessageType() {
        return messageType;
    }
}
