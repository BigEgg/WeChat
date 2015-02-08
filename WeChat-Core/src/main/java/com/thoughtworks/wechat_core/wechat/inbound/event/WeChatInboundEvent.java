package com.thoughtworks.wechat_core.wechat.inbound.event;

import com.thoughtworks.wechat_core.wechat.inbound.WeChatInbound;

public interface WeChatInboundEvent extends WeChatInbound {
    String getEventType();
}
