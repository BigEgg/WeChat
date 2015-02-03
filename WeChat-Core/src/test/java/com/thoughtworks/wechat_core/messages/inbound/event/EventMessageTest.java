package com.thoughtworks.wechat_core.messages.inbound.event;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageType;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class EventMessageTest {
    @Test
    public void testConstructor() throws Exception {
        MockEventMessage eventMessage = new MockEventMessage(DateTime.now(), EventType.SUBSCRIBE);

        assertThat(eventMessage.getMessageType(), equalTo(InboundMessageType.EVENT));
        assertThat(eventMessage.getCreatedTime(), notNullValue());
        assertThat(eventMessage.getEventType(), equalTo(EventType.SUBSCRIBE));
    }

    private class MockEventMessage extends EventMessage {
        public MockEventMessage(DateTime createdTime, EventType eventType) {
            super(createdTime, eventType);
        }
    }
}