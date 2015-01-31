package com.thoughtworks.wechat_core.messages.inbound;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageType;

public class InboundMessageEnvelop {
    private String fromUser;
    private String toUser;
    private InboundMessage message;

    public InboundMessageEnvelop() {
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public InboundMessage getMessage() {
        return message;
    }

    public OutboundMessageEnvelop reply(OutboundMessage message, OutboundMessageType messageType) {
        return new OutboundMessageEnvelop(toUser, fromUser, messageType, message);
    }
}
