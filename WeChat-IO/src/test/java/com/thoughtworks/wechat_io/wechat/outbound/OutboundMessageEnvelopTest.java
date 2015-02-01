package com.thoughtworks.wechat_io.wechat.outbound;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class OutboundMessageEnvelopTest {
    @Test
    public void testConstructor() throws Exception {
        OutboundMessage message = mock(OutboundMessage.class);
        OutboundMessageEnvelop envelop = new OutboundMessageEnvelop("fromUser", "toUser", message);
        assertThat(envelop.getFromUser(), equalTo("fromUser"));
        assertThat(envelop.getToUser(), equalTo("toUser"));
        assertThat(envelop.getMessage(), equalTo(message));
    }
}