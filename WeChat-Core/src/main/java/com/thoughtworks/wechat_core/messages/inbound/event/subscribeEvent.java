package com.thoughtworks.wechat_core.messages.inbound.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class SubscribeEvent {
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

    public SubscribeEvent(String toUser, String fromUser, int createdTime, String messageType, String eventType) {
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.createdTime = createdTime;
        this.messageType = messageType;
        this.eventType = eventType;
    }

    public String getToUser() {
        return toUser;
    }

    public String getFromUser() {
        return fromUser;
    }

    public int getCreatedTime() {
        return createdTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getEventType() {
        return eventType;
    }
}
