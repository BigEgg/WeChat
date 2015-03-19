package com.thoughtworks.wechat_core.messages.inbound.messages;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageType;
import com.thoughtworks.wechat_core.wechat.inbound.message.WeChatInboundTextMessage;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InboundTextMessageTest {
    @Test
    public void testConstructor() throws Exception {
        final WeChatInboundTextMessage weChatInboundTextMessage = new WeChatInboundTextMessage("toUser", "fromUser", 1422800623, "text", "content", 1L);
        final InboundTextMessage inboundTextMessage = new InboundTextMessage(weChatInboundTextMessage);

        assertThat(inboundTextMessage.getMessageType(), equalTo(InboundMessageType.TEXT));
        assertThat(inboundTextMessage.getCreatedTime(), notNullValue());
        assertThat(inboundTextMessage.getMessageId(), equalTo(1L));
        assertThat(inboundTextMessage.getContent(), equalTo("content"));
    }
}