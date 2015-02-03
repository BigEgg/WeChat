package com.thoughtworks.wechat_core.messages.outbound.messages;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageType;
import org.joda.time.DateTime;

public class OutboundTextMessage extends OutboundMessageBase {
    private String content;

    public OutboundTextMessage(String content) {
        super(OutboundMessageType.TEXT, DateTime.now());
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
