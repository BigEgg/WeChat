package com.thoughtworks.wechat_application.models.systemMessage;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;

public interface SystemMessage {
    SystemMessageType getType();

    OutboundMessage toOutboundMessage();
}
