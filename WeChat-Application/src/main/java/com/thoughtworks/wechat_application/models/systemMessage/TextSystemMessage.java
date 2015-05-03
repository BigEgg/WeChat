package com.thoughtworks.wechat_application.models.systemMessage;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_core.messages.outbound.messages.OutboundTextMessage;

public class TextSystemMessage implements SystemMessage {
    private final String content;

    public TextSystemMessage(String content) {
        this.content = content;
    }

    @Override
    public SystemMessageType getType() {
        return SystemMessageType.TEXT_MESSAGE;
    }

    public OutboundMessage toOutboundMessage() {
        return new OutboundTextMessage(content);
    }

    public String getContent() {
        return content;
    }
}
