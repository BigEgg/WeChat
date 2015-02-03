package com.thoughtworks.wechat_core.messages.outbound.messages;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageType;
import com.thoughtworks.wechat_core.wechat.outbound.WeChatTextMessage;
import com.thoughtworks.xstream.XStream;
import org.junit.Test;

import static com.thoughtworks.wechat_core.util.xstream.XStreamExtension.createXStreamWithCData;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class OutboundTextMessageTest {
    @Test
    public void testConstructor() throws Exception {
        OutboundTextMessage textMessage = new OutboundTextMessage("content");
        assertThat(textMessage.getMessageType(), equalTo(OutboundMessageType.TEXT));
        assertThat(textMessage.getCreatedTime(), notNullValue());
        assertThat(textMessage.getContent(), equalTo("content"));
    }

    @Test
    public void testToWeChat() throws Exception {
        OutboundTextMessage textMessage = new OutboundTextMessage("content");
        OutboundMessageEnvelop envelop = new OutboundMessageEnvelop("fromUser", "toUser", textMessage);

        WeChatTextMessage weChatTextMessage = (WeChatTextMessage) textMessage.toWeChat(envelop);
        XStream xStream = createXStreamWithCData();
        xStream.processAnnotations(WeChatTextMessage.class);
        String xmlMessage = xStream.toXML(weChatTextMessage);

        assertThat(xmlMessage.contains("<xml>"), is(true));
        assertThat(xmlMessage.contains("<ToUserName><![CDATA[toUser]]></ToUserName>"), is(true));
        assertThat(xmlMessage.contains("<FromUserName><![CDATA[fromUser]]></FromUserName>"), is(true));
        assertThat(xmlMessage.contains("<CreateTime>"), is(true));
        assertThat(xmlMessage.contains("</CreateTime>"), is(true));
        assertThat(xmlMessage.contains("<MsgType><![CDATA[text]]></MsgType>"), is(true));
        assertThat(xmlMessage.contains("<Content><![CDATA[content]]></Content>"), is(true));
        assertThat(xmlMessage.contains("</xml>"), is(true));
    }
}