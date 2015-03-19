package com.thoughtworks.wechat_core.messages.inbound.event;

import com.thoughtworks.wechat_core.wechat.inbound.event.WeChatSubscribeEvent;

import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUTCDateTime;

public class InboundSubscribeEvent extends InboundEventMessage {
    public InboundSubscribeEvent(final WeChatSubscribeEvent event) {
        super(toUTCDateTime(event.getCreatedTime()), EventType.SUBSCRIBE);
    }
}
