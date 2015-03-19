package com.thoughtworks.wechat_core.messages.outbound;

import com.thoughtworks.wechat_core.wechat.outbound.WeChatOutboundTextMessage;
import com.thoughtworks.xstream.XStream;
import org.junit.Test;

import java.util.Optional;

import static com.thoughtworks.wechat_core.util.xstream.XStreamExtension.createXStreamWithCData;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OutboundMessageEnvelopTest {
    @Test
    public void testConstructor() throws Exception {
        final OutboundMessage message = mock(OutboundMessage.class);
        final OutboundMessageEnvelop envelop = new OutboundMessageEnvelop("fromUser", "toUser", Optional.of(message));
        assertThat(envelop.getFromUser(), equalTo("fromUser"));
        assertThat(envelop.getToUser(), equalTo("toUser"));
        assertThat(envelop.getMessage().isPresent(), equalTo(true));
        assertThat(envelop.getMessage().get(), equalTo(message));

        when(message.toWeChat(eq(envelop))).thenReturn(new WeChatOutboundTextMessage("toUser", "fromUser", 12345678, "text", "hello"));
        final WeChatOutboundTextMessage weChatOutboundTextMessage = (WeChatOutboundTextMessage) envelop.toWeChat();
        final XStream xStream = createXStreamWithCData();
        xStream.processAnnotations(WeChatOutboundTextMessage.class);
        final String xmlMessage = xStream.toXML(weChatOutboundTextMessage);

        assertThat(xmlMessage.contains("<xml>"), is(true));
        assertThat(xmlMessage.contains("<ToUserName><![CDATA[toUser]]></ToUserName>"), is(true));
        assertThat(xmlMessage.contains("<FromUserName><![CDATA[fromUser]]></FromUserName>"), is(true));
        assertThat(xmlMessage.contains("<CreateTime>12345678</CreateTime>"), is(true));
        assertThat(xmlMessage.contains("<MsgType><![CDATA[text]]></MsgType>"), is(true));
        assertThat(xmlMessage.contains("<Content><![CDATA[hello]]></Content>"), is(true));
        assertThat(xmlMessage.contains("</xml>"), is(true));
    }
}