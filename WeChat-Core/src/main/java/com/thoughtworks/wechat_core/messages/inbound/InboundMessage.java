package com.thoughtworks.wechat_core.messages.inbound;

import org.joda.time.DateTime;

public interface InboundMessage {
    InboundMessageType getMessageType();

    DateTime getCreatedTime();
}
