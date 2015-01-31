package com.thoughtworks.wechat_core.messages.inbound;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public abstract class InboundMessage {
    @XStreamAlias("ToUserName")
    private String toUser;
    @XStreamAlias("FromUserName")
    private String fromUser;
    @XStreamAlias("CreateTime")
    private int createdTime;
    @XStreamAlias("MsgType")
    private String messageType;

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
}
