package com.thoughtworks.wechat_core.messages.inbound.event;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessage;

public class SubscribeEvent extends InboundMessage {
    private String eventType;

    public String getEventType() {
        return eventType;
    }
}
