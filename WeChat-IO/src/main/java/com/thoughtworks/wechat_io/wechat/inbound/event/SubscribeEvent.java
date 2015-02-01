package com.thoughtworks.wechat_io.wechat.inbound.event;

import static com.thoughtworks.wechat_io.util.DateTimeExtension.toUTCDateTime;

public class SubscribeEvent extends EventMessage {
    public SubscribeEvent(com.thoughtworks.wechat_core.messages.inbound.event.SubscribeEvent event) {
        super(toUTCDateTime(event.getCreatedTime()), EventType.SUBSCRIBE);
    }
}
