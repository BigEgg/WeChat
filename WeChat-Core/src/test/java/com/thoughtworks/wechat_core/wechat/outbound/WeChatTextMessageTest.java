package com.thoughtworks.wechat_core.wechat.outbound;

import com.thoughtworks.xstream.XStream;
import org.junit.Test;

import static com.thoughtworks.wechat_core.util.xstream.XStreamExtension.createXStreamWithCData;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class WeChatTextMessageTest {
    @Test
    public void testSerialize() {
        WeChatTextMessage message = new WeChatTextMessage("toUser", "fromUser", 12345678, "text", "hello");

        XStream xStream = createXStreamWithCData();
        xStream.processAnnotations(WeChatTextMessage.class);
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