package com.thoughtworks.wechat_core.messages.inbound.messages;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageType;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InboundMessageBaseTest {
    @Test
    public void testConstructor() throws Exception {
        final MockInboundMessage mockInboundMessage = new MockInboundMessage(InboundMessageType.TEXT, DateTime.now(), 1L);

        assertThat(mockInboundMessage.getMessageType(), equalTo(InboundMessageType.TEXT));
        assertThat(mockInboundMessage.getCreatedTime(), notNullValue());
        assertThat(mockInboundMessage.getMessageId(), equalTo(1L));
    }

    private class MockInboundMessage extends InboundMessageBase {
        public MockInboundMessage(InboundMessageType messageType, DateTime createdTime, long messageId) {
            super(messageType, createdTime, messageId);
        }
    }
}