package com.thoughtworks.wechat_core.wechat.inbound.message;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageType;
import com.thoughtworks.wechat_core.messages.inbound.messages.InboundTextMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class WeChatInboundTextMessageTest {
    @Test
    public void deserializeTest() throws Exception {
        final String inputXML = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "<CreateTime>1348831860</CreateTime>\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "<Content><![CDATA[this is a test]]></Content>\n" +
                "<MsgId>1234567890123456</MsgId>\n" +
                "</xml>";

        final XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(WeChatInboundTextMessage.class);
        final WeChatInboundTextMessage message = (WeChatInboundTextMessage) xstream.fromXML(inputXML);

        assertThat(message, notNullValue());
        assertThat(message.getToUser(), equalTo("toUser"));
        assertThat(message.getFromUser(), equalTo("fromUser"));
        assertThat(message.getCreatedTime(), equalTo(1348831860));
        assertThat(message.getMessageType(), equalTo("text"));
        assertThat(message.getContent(), equalTo("this is a test"));
        assertThat(message.getMessageId(), equalTo(1234567890123456L));
    }

    @Test
    public void testToEnvelop() throws Exception {
        final String inputXML = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "<CreateTime>1348831860</CreateTime>\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "<Content><![CDATA[this is a test]]></Content>\n" +
                "<MsgId>1234567890123456</MsgId>\n" +
                "</xml>";

        final XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(WeChatInboundTextMessage.class);
        final WeChatInboundTextMessage event = (WeChatInboundTextMessage) xstream.fromXML(inputXML);

        final InboundMessageEnvelop envelop = event.toEnvelop();
        assertThat(envelop, notNullValue());
        assertThat(envelop.getToUser(), equalTo("toUser"));
        assertThat(envelop.getFromUser(), equalTo("fromUser"));

        final InboundTextMessage message = (InboundTextMessage) envelop.getMessage();
        assertThat(message, notNullValue());
        assertThat(message.getCreatedTime().toString("yyyy-MM-dd HH:mm:ss"), equalTo("2012-09-28 11:31:00"));
        assertThat(message.getMessageType(), equalTo(InboundMessageType.TEXT));
        assertThat(message.getContent(), equalTo("this is a test"));
        assertThat(message.getMessageId(), equalTo(1234567890123456L));
    }

}