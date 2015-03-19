package com.thoughtworks.wechat_core.messages.outbound.messages;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageType;
import com.thoughtworks.wechat_core.wechat.outbound.WeChatOutbound;
import com.thoughtworks.wechat_core.wechat.outbound.WeChatOutboundTextMessage;
import org.joda.time.DateTime;

import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUnixTimestampInt;

public class OutboundTextMessage extends OutboundMessageBase {
    private String content;

    public OutboundTextMessage(String content) {
        super(OutboundMessageType.TEXT, DateTime.now());
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public WeChatOutbound toWeChat(OutboundMessageEnvelop envelop) {
        return new WeChatOutboundTextMessage(envelop.getToUser(), envelop.getFromUser(), toUnixTimestampInt(this.getCreatedTime()), this.getMessageType().toString().toLowerCase(), this.getContent());
    }
}
