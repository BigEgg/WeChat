package com.thoughtworks.wechat_core.messages.outbound;

import com.thoughtworks.wechat_core.util.xstream.XStreamCData;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class TextMessage {
    @XStreamAlias("ToUserName")
    @XStreamCData
    private String toUser;
    @XStreamAlias("FromUserName")
    @XStreamCData
    private String fromUser;
    @XStreamAlias("CreateTime")
    private int createdTime;
    @XStreamAlias("MsgType")
    @XStreamCData
    private String messageType;
    @XStreamAlias("Content")
    @XStreamCData
    private String content;

    public TextMessage(String toUser, String fromUser, int createdTime, String messageType, String content) {
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.createdTime = createdTime;
        this.messageType = messageType;
        this.content = content;
    }
}