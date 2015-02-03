package com.thoughtworks.wechat_core.messages.outbound;

import com.thoughtworks.wechat_core.wechat.outbound.WeChatOutbound;
import org.joda.time.DateTime;

public interface OutboundMessage {
    OutboundMessageType getMessageType();

    DateTime getCreatedTime();

    WeChatOutbound toWeChat(OutboundMessageEnvelop envelop);
}
