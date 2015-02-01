package com.thoughtworks.wechat_io.wechat.outbound;

import org.joda.time.DateTime;

public interface OutboundMessage {
    OutboundMessageType getMessageType();

    DateTime getCreatedTime();
}
