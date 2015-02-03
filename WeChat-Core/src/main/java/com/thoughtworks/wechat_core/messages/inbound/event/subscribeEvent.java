package com.thoughtworks.wechat_core.messages.inbound.event;

import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUTCDateTime;

public class SubscribeEvent extends EventMessage {
    public SubscribeEvent(com.thoughtworks.wechat_core.wechat.inbound.event.SubscribeEvent event) {
        super(toUTCDateTime(event.getCreatedTime()), EventType.SUBSCRIBE);
    }
}
