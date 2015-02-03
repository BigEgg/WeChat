package com.thoughtworks.wechat_core.messages.outbound;

import com.thoughtworks.wechat_core.wechat.outbound.WeChatOutbound;

public class OutboundMessageEnvelop {
    private String fromUser;
    private String toUser;
    private OutboundMessage message;

    public OutboundMessageEnvelop(String fromUser, String toUser, OutboundMessage message) {
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

    public OutboundMessage getMessage() {
        return message;
    }

    public WeChatOutbound toWeChat() {
        return message.toWeChat(this);
    }
}
