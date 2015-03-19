package com.thoughtworks.wechat_core.messages.inbound.messages;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessage;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageType;
import org.joda.time.DateTime;

public abstract class InboundMessageBase implements InboundMessage {
    private InboundMessageType messageType;
    private DateTime createdTime;
    private long messageId;

    public InboundMessageBase(final InboundMessageType messageType,
                              final DateTime createdTime,
                              final long messageId) {
        this.messageType = messageType;
        this.createdTime = createdTime;
        this.messageId = messageId;
    }

    @Override
    public InboundMessageType getMessageType() {
        return messageType;
    }

    @Override
    public DateTime getCreatedTime() {
        return createdTime;
    }

    public long getMessageId() {
        return messageId;
    }
}
