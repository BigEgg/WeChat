package com.thoughtworks.wechat_core.messages.outbound;

public class TextMessage implements OutboundMessage {
    private OutboundMessageType messageType;
    private int createdTime;
    private String message;

    public TextMessage(OutboundMessageType messageType, int createdTime, String message) {
        this.messageType = messageType;
        this.createdTime = createdTime;
        this.message = message;
    }

    @Override
    public int getCreatedTime() {
        return createdTime;
    }

    @Override
    public OutboundMessageType getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }
}
