package com.thoughtworks.wechat_core.messages.outbound;

import com.thoughtworks.xstream.XStream;
import org.junit.Test;

import static com.thoughtworks.wechat_core.util.xstream.XStreamExtension.createXStreamWithCData;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

;

public class TextMessageTest {
    @Test
    public void testSerialize() {
        TextMessage message = new TextMessage("toUser", "fromUser", 12345678, "text", "hello");

        XStream xStream = createXStreamWithCData();
        xStream.processAnnotations(TextMessage.class);
        String xmlMessage = xStream.toXML(message);

        assertThat(xmlMessage.contains("<xml>"), is(true));
        assertThat(xmlMessage.contains("<ToUserName><![CDATA[toUser]]></ToUserName>"), is(true));
        assertThat(xmlMessage.contains("<FromUserName><![CDATA[fromUser]]></FromUserName>"), is(true));
        assertThat(xmlMessage.contains("<CreateTime>12345678</CreateTime>"), is(true));
        assertThat(xmlMessage.contains("<MsgType><![CDATA[text]]></MsgType>"), is(true));
        assertThat(xmlMessage.contains("<Content><![CDATA[hello]]></Content>"), is(true));
        assertThat(xmlMessage.contains("</xml>"), is(true));

    }
}