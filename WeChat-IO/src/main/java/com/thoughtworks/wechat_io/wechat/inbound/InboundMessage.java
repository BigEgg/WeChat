package com.thoughtworks.wechat_io.wechat.inbound;

import org.joda.time.DateTime;

public interface InboundMessage {
    InboundMessageType getMessageType();

    DateTime getCreatedTime();
}
