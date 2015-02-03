package com.thoughtworks.wechat_core.wechat.inbound.event;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

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

        assertThat(event, notNullValue());
        assertThat(event.getToUser(), equalTo("toUser"));
        assertThat(event.getFromUser(), equalTo("FromUser"));
        assertThat(event.getFromUser(), equalTo("FromUser"));
        assertThat(event.getCreatedTime(), equalTo(123456789));
        assertThat(event.getMessageType(), equalTo("event"));
        assertThat(event.getEventType(), equalTo("subscribe"));
    }
}