package com.thoughtworks.wechat_core.messages.outbound;

public class OutboundMessageEnvelop {
    private String fromUser;
    private String toUser;
    private OutboundMessage message;

    public OutboundMessageEnvelop(String fromUser, String toUser, OutboundMessageType messageType, OutboundMessage message) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.message = message;
    }
}
