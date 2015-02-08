package com.thoughtworks.wechat_core.wechat.inbound;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;

public interface WeChatInbound {
    String getToUser();

    String getFromUser();

    int getCreatedTime();

    String getMessageType();

    InboundMessageEnvelop toEnvelop();
}
