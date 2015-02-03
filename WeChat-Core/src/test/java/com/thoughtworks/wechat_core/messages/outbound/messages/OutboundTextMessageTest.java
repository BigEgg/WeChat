package com.thoughtworks.wechat_core.messages.outbound.messages;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageType;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class OutboundTextMessageTest {
    @Test
    public void testConstructor() throws Exception {
        OutboundTextMessage textMessage = new OutboundTextMessage("content");
        assertThat(textMessage.getMessageType(), equalTo(OutboundMessageType.TEXT));
        assertThat(textMessage.getCreatedTime(), notNullValue());
        assertThat(textMessage.getContent(), equalTo("content"));
    }
}