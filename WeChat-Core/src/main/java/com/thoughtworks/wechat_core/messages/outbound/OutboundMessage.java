package com.thoughtworks.wechat_core.messages.outbound;

import org.joda.time.DateTime;

public interface OutboundMessage {
    OutboundMessageType getMessageType();

    DateTime getCreatedTime();
}
