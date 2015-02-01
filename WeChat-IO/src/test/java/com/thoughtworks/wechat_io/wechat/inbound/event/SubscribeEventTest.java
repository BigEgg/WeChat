package com.thoughtworks.wechat_io.wechat.inbound.event;

import com.thoughtworks.wechat_io.wechat.inbound.InboundMessageType;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SubscribeEventTest {
    @Test
    public void testConstructor() throws Exception {
        com.thoughtworks.wechat_core.messages.inbound.event.SubscribeEvent event = new com.thoughtworks.wechat_core.messages.inbound.event.SubscribeEvent("toUser", "fromUser", 1422800623, "event", "subscribe");
        SubscribeEvent subscribeEvent = new SubscribeEvent(event);

        assertThat(subscribeEvent.getMessageType(), equalTo(InboundMessageType.EVENT));
        assertThat(subscribeEvent.getCreatedTime().toString("yyyy-MM-dd HH:mm:ss"), equalTo("2015-02-01 14:23:43"));
        assertThat(subscribeEvent.getEventType(), equalTo(EventType.SUBSCRIBE));
    }
}