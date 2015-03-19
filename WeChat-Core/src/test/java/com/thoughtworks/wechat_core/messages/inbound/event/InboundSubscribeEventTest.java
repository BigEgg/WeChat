package com.thoughtworks.wechat_core.messages.inbound.event;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageType;
import com.thoughtworks.wechat_core.wechat.inbound.event.WeChatSubscribeEvent;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class InboundSubscribeEventTest {
    @Test
    public void testConstructor() throws Exception {
        final WeChatSubscribeEvent event = new WeChatSubscribeEvent("toUser", "fromUser", 1422800623, "event", "subscribe");
        final InboundSubscribeEvent subscribeEvent = new InboundSubscribeEvent(event);

        assertThat(subscribeEvent.getMessageType(), equalTo(InboundMessageType.EVENT));
        assertThat(subscribeEvent.getCreatedTime().toString("yyyy-MM-dd HH:mm:ss"), equalTo("2015-02-01 14:23:43"));
        assertThat(subscribeEvent.getEventType(), equalTo(EventType.SUBSCRIBE));
    }
}