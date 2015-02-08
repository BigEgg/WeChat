package com.thoughtworks.wechat_core.wechat.inbound;

import com.thoughtworks.wechat_core.wechat.inbound.event.WeChatInboundEvent;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WeChatInboundDeserializerTest {
    @Test
    public void can_handle_subscribe_event() {
        String message = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>1422800623</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[subscribe]]></Event>\n" +
                "</xml>";

        Optional<WeChatInbound> weChatInboundOpt = WeChatInboundDeserializer.tryDeserialize(message);
        assertThat(weChatInboundOpt.isPresent(), equalTo(true));

        WeChatInbound weChatInbound = weChatInboundOpt.get();
        assertThat(weChatInbound.getToUser(), equalTo("toUser"));
        assertThat(weChatInbound.getFromUser(), equalTo("FromUser"));
        assertThat(weChatInbound.getCreatedTime(), equalTo(1422800623));
        assertThat(weChatInbound.getMessageType(), equalTo("event"));

        WeChatInboundEvent event = (WeChatInboundEvent) weChatInbound;
        assertThat(event.getEventType(), equalTo("subscribe"));
    }
}