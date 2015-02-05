package com.thoughtworks.wechat_core.wechat.outbound;

import com.thoughtworks.wechat_core.util.xstream.XStreamCData;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import static com.thoughtworks.wechat_core.util.xstream.XStreamExtension.createXStreamWithCData;

@XStreamAlias("xml")
public class WeChatTextMessage implements WeChatOutbound {
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

    public WeChatTextMessage(String toUser, String fromUser, int createdTime, String messageType, String content) {
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.createdTime = createdTime;
        this.messageType = messageType;
        this.content = content;
    }

    @Override
    public String toString() {
        XStream xStream = createXStreamWithCData();
        xStream.processAnnotations(WeChatTextMessage.class);
        return xStream.toXML(this);
    }
}