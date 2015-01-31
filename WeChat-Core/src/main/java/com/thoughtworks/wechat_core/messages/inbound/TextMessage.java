package com.thoughtworks.wechat_core.messages.inbound;

public class TextMessage implements InboundMessage {
    private InboundMessageType messageType;
    private int createdTime;
    private long messageId;
    private String message;

    @Override
    public InboundMessageType getMessageType() {
        return messageType;
    }

    @Override
    public int getCreatedTime() {
        return createdTime;
    }

    @Override
    public long getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }
}
