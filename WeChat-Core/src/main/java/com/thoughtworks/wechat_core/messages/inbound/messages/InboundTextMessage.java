package com.thoughtworks.wechat_core.messages.inbound.messages;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageType;
import com.thoughtworks.wechat_core.wechat.inbound.message.WeChatInboundTextMessage;

import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUTCDateTime;

public class InboundTextMessage extends InboundMessageBase {
    private final String content;

    public InboundTextMessage(WeChatInboundTextMessage message) {
        super(InboundMessageType.TEXT, toUTCDateTime(message.getCreatedTime()), message.getMessageId());
        this.content = message.getContent();
    }

    public String getContent() {
        return content;
    }
}
