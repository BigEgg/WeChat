package com.thoughtworks.wechat_core.wechat.inbound;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;

public interface WeChatInbound {
    InboundMessageEnvelop toEnvelop();
}
