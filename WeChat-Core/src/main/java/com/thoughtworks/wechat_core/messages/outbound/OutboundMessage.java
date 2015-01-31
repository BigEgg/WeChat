package com.thoughtworks.wechat_core.messages.outbound;

public interface OutboundMessage {
    OutboundMessageType getMessageType();

    int getCreatedTime();
}
