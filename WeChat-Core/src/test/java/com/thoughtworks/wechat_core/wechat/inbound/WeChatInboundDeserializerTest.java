package com.thoughtworks.wechat_core.wechat.inbound;

import com.thoughtworks.wechat_core.wechat.inbound.event.WeChatInboundEvent;
import com.thoughtworks.wechat_core.wechat.inbound.message.WeChatInboundTextMessage;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WeChatInboundDeserializerTest {
    @Test
    public void can_handle_subscribe_event() {
        final String inputXML = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>1422800623</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[subscribe]]></Event>\n" +
                "</xml>";

        final Optional<WeChatInbound> weChatInboundOpt = WeChatInboundDeserializer.tryDeserialize(inputXML);
        assertThat(weChatInboundOpt.isPresent(), equalTo(true));

        final WeChatInbound weChatInbound = weChatInboundOpt.get();
        assertThat(weChatInbound.getToUser(), equalTo("toUser"));
        assertThat(weChatInbound.getFromUser(), equalTo("FromUser"));
        assertThat(weChatInbound.getCreatedTime(), equalTo(1422800623));
        assertThat(weChatInbound.getMessageType(), equalTo("event"));

        final WeChatInboundEvent event = (WeChatInboundEvent) weChatInbound;
        assertThat(event.getEventType(), equalTo("subscribe"));
    }

    @Test
    public void can_handle_inbound_text_message() {
        final String inputXML = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "<CreateTime>1348831860</CreateTime>\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "<Content><![CDATA[this is a test]]></Content>\n" +
                "<MsgId>1234567890123456</MsgId>\n" +
                "</xml>";

        final Optional<WeChatInbound> weChatInboundOpt = WeChatInboundDeserializer.tryDeserialize(inputXML);
        assertThat(weChatInboundOpt.isPresent(), equalTo(true));

        final WeChatInbound weChatInbound = weChatInboundOpt.get();
        assertThat(weChatInbound.getToUser(), equalTo("toUser"));
        assertThat(weChatInbound.getFromUser(), equalTo("fromUser"));
        assertThat(weChatInbound.getCreatedTime(), equalTo(1348831860));
        assertThat(weChatInbound.getMessageType(), equalTo("text"));

        final WeChatInboundTextMessage message = (WeChatInboundTextMessage) weChatInbound;
        assertThat(message.getContent(), equalTo("this is a test"));
        assertThat(message.getMessageId(), equalTo(1234567890123456L));
    }
}