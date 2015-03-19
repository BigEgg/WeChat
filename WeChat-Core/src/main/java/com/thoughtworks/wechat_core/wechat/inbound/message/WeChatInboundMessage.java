package com.thoughtworks.wechat_core.wechat.inbound.message;

import com.thoughtworks.wechat_core.wechat.inbound.WeChatInbound;

public interface WeChatInboundMessage extends WeChatInbound {
    long getMessageId();
}
