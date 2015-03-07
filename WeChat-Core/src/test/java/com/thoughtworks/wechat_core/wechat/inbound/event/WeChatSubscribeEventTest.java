package com.thoughtworks.wechat_core.wechat.inbound.event;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageType;
import com.thoughtworks.wechat_core.messages.inbound.event.EventType;
import com.thoughtworks.wechat_core.messages.inbound.event.InboundSubscribeEvent;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class WeChatSubscribeEventTest {
    @Test
    public void deserializeTest() throws Exception {
        String message = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[subscribe]]></Event>\n" +
                "</xml>";

        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(WeChatSubscribeEvent.class);
        WeChatSubscribeEvent event = (WeChatSubscribeEvent) xstream.fromXML(message);

        assertThat(event, notNullValue());
        assertThat(event.getToUser(), equalTo("toUser"));
        assertThat(event.getFromUser(), equalTo("FromUser"));
        assertThat(event.getCreatedTime(), equalTo(123456789));
        assertThat(event.getMessageType(), equalTo("event"));
        assertThat(event.getEventType(), equalTo("subscribe"));
    }

    @Test
    public void testToEnvelop() throws Exception {
        String message = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>1422800623</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[subscribe]]></Event>\n" +
                "</xml>";

        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(WeChatSubscribeEvent.class);
        WeChatSubscribeEvent event = (WeChatSubscribeEvent) xstream.fromXML(message);

        InboundMessageEnvelop envelop = event.toEnvelop();
        assertThat(envelop, notNullValue());
        assertThat(envelop.getToUser(), equalTo("toUser"));
        assertThat(envelop.getFromUser(), equalTo("FromUser"));

        InboundSubscribeEvent subscribeEvent = (InboundSubscribeEvent) envelop.getMessage();
        assertThat(subscribeEvent, notNullValue());
        assertThat(subscribeEvent.getCreatedTime().toString("yyyy-MM-dd HH:mm:ss"), equalTo("2015-02-01 14:23:43"));
        assertThat(subscribeEvent.getMessageType(), equalTo(InboundMessageType.EVENT));
        assertThat(subscribeEvent.getEventType(), equalTo(EventType.SUBSCRIBE));
    }
}