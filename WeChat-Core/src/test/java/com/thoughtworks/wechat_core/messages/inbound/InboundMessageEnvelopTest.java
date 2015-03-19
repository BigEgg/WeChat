package com.thoughtworks.wechat_core.messages.inbound;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageEnvelop;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class InboundMessageEnvelopTest {
    @Test
    public void testConstructor() throws Exception {
        final InboundMessage message = mock(InboundMessage.class);
        final InboundMessageEnvelop envelop = new InboundMessageEnvelop("fromUser", "toUser", message);

        assertThat(envelop.getFromUser(), equalTo("fromUser"));
        assertThat(envelop.getToUser(), equalTo("toUser"));
        assertThat(envelop.getMessage(), equalTo(message));
    }

    @Test
    public void testReply() throws Exception {
        final InboundMessage message = mock(InboundMessage.class);
        final InboundMessageEnvelop envelop = new InboundMessageEnvelop("user1", "user2", message);
        final OutboundMessage outboundMessage = mock(OutboundMessage.class);

        final OutboundMessageEnvelop outboundEnvelop = envelop.reply(Optional.of(outboundMessage));
        assertThat(outboundEnvelop, notNullValue());
        assertThat(outboundEnvelop.getFromUser(), equalTo("user2"));
        assertThat(outboundEnvelop.getToUser(), equalTo("user1"));
        assertThat(outboundEnvelop.getMessage().isPresent(), equalTo(true));
        assertThat(outboundEnvelop.getMessage().get(), equalTo(outboundMessage));
    }
}