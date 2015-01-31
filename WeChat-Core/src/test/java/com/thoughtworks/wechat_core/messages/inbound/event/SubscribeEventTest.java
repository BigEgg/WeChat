package com.thoughtworks.wechat_core.messages.inbound.event;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SubscribeEventTest {
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
        xstream.processAnnotations(SubscribeEvent.class);
        SubscribeEvent event = (SubscribeEvent) xstream.fromXML(message);

        assertNotNull(event);
        assertEquals("toUser", event.getToUser());
        assertEquals("FromUser", event.getFromUser());
        assertEquals(123456789, event.getCreatedTime());
        assertEquals("event", event.getMessageType());
        assertEquals("subscribe", event.getEventType());
    }
}