package com.thoughtworks.wechat_core.messages.outbound.messages;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageType;
import org.joda.time.DateTime;

public abstract class OutboundMessageBase {
    private OutboundMessageType messageType;
    private DateTime createdTime;

    public OutboundMessageBase(OutboundMessageType messageType, DateTime createdTime) {
        this.messageType = messageType;
        this.createdTime = createdTime;
    }

    public OutboundMessageType getMessageType() {
        return messageType;
    }

    public DateTime getCreatedTime() {
        return createdTime;
    }
}
