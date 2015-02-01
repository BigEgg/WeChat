package com.thoughtworks.wechat_io.wechat.inbound;

import com.thoughtworks.wechat_io.wechat.outbound.OutboundMessage;
import com.thoughtworks.wechat_io.wechat.outbound.OutboundMessageEnvelop;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class InboundMessageEnvelopTest {
    @Test
    public void testConstructor() throws Exception {
        InboundMessage message = mock(InboundMessage.class);
        InboundMessageEnvelop envelop = new InboundMessageEnvelop("fromUser", "toUser", message);

        assertThat(envelop.getFromUser(), equalTo("fromUser"));
        assertThat(envelop.getToUser(), equalTo("toUser"));
        assertThat(envelop.getMessage(), equalTo(message));
    }

    @Test
    public void testReply() throws Exception {
        InboundMessage message = mock(InboundMessage.class);
        InboundMessageEnvelop envelop = new InboundMessageEnvelop("user1", "user2", message);
        OutboundMessage outboundMessage = mock(OutboundMessage.class);

        OutboundMessageEnvelop outboundEnvelop = envelop.reply(outboundMessage);
        assertThat(outboundEnvelop.getFromUser(), equalTo("user2"));
        assertThat(outboundEnvelop.getToUser(), equalTo("user1"));
        assertThat(outboundEnvelop.getMessage(), equalTo(outboundMessage));
    }
}