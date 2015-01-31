package com.thoughtworks.wechat_core.messages.inbound;

public interface InboundMessage {
    InboundMessageType getMessageType();

    int getCreatedTime();

    long getMessageId();
}
