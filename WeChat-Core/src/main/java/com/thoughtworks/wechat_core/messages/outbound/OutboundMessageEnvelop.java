package com.thoughtworks.wechat_core.messages.outbound;

import com.thoughtworks.wechat_core.wechat.outbound.WeChatOutboundEmptyMessage;
import com.thoughtworks.wechat_core.wechat.outbound.WeChatOutbound;

import java.util.Optional;

public class OutboundMessageEnvelop {
    private String fromUser;
    private String toUser;
    private Optional<OutboundMessage> message;

    public OutboundMessageEnvelop(String fromUser, String toUser, Optional<OutboundMessage> message) {
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

    public Optional<OutboundMessage> getMessage() {
        return message;
    }

    public WeChatOutbound toWeChat() {
        return message.isPresent()
                ? message.get().toWeChat(this)
                : new WeChatOutboundEmptyMessage();
    }
}
