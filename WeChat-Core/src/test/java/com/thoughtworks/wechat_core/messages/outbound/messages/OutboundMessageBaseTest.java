package com.thoughtworks.wechat_core.messages.outbound.messages;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageType;
import com.thoughtworks.wechat_core.wechat.outbound.WeChatOutbound;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class OutboundMessageBaseTest {
    @Test
    public void testConstructor() throws Exception {
        MockOutboundMessage message = new MockOutboundMessage(OutboundMessageType.TEXT, DateTime.now());
        assertThat(message.getMessageType(), equalTo(OutboundMessageType.TEXT));
        assertThat(message.getCreatedTime(), notNullValue());
    }

    public class MockOutboundMessage extends OutboundMessageBase {
        public MockOutboundMessage(OutboundMessageType messageType, DateTime createdTime) {
            super(messageType, createdTime);
        }

        @Override
        public WeChatOutbound toWeChat(OutboundMessageEnvelop envelop) {
            return null;
        }
    }
}