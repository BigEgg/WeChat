package com.thoughtworks.wechat_core.messages.inbound;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageEnvelop;

import java.util.Optional;

public class InboundMessageEnvelop {
    private String fromUser;
    private String toUser;
    private InboundMessage message;

    public InboundMessageEnvelop(final String fromUser,
                                 final String toUser,
                                 final InboundMessage message) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.message = message;
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

    public OutboundMessageEnvelop reply(final Optional<OutboundMessage> message) {
        return new OutboundMessageEnvelop(toUser, fromUser, message);
    }
}
