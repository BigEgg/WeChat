package com.thoughtworks.wechat_core.wechat.inbound.event;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.inbound.event.InboundSubscribeEvent;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WeChatSubscribeEvent implements WeChatInboundEvent {
    @XStreamAlias("ToUserName")
    private String toUser;
    @XStreamAlias("FromUserName")
    private String fromUser;
    @XStreamAlias("CreateTime")
    private int createdTime;
    @XStreamAlias("MsgType")
    private String messageType;
    @XStreamAlias("Event")
    private String eventType;

    public WeChatSubscribeEvent(String toUser, String fromUser, int createdTime, String messageType, String eventType) {
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.createdTime = createdTime;
        this.messageType = messageType;
        this.eventType = eventType;
    }

    @Override
    public String getToUser() {
        return toUser;
    }

    @Override
    public String getFromUser() {
        return fromUser;
    }

    @Override
    public int getCreatedTime() {
        return createdTime;
    }

    @Override
    public String getMessageType() {
        return messageType;
    }

    @Override
    public InboundMessageEnvelop toEnvelop() {
        InboundSubscribeEvent subscribeEvent = new InboundSubscribeEvent(this);
        return new InboundMessageEnvelop(fromUser, toUser, subscribeEvent);
    }

    @Override
    public String getEventType() {
        return eventType;
    }
}
