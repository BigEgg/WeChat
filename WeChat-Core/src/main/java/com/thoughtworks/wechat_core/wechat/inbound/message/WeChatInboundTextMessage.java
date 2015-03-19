package com.thoughtworks.wechat_core.wechat.inbound.message;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.inbound.messages.InboundTextMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WeChatInboundTextMessage implements WeChatInboundMessage {
    @XStreamAlias("ToUserName")
    private String toUser;
    @XStreamAlias("FromUserName")
    private String fromUser;
    @XStreamAlias("CreateTime")
    private int createdTime;
    @XStreamAlias("MsgType")
    private String messageType;
    @XStreamAlias("Content")
    private String content;
    @XStreamAlias("MsgId")
    private long messageId;

    public WeChatInboundTextMessage(final String toUser,
                                    final String fromUser,
                                    final int createdTime,
                                    final String messageType,
                                    final String content, long messageId) {
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.createdTime = createdTime;
        this.messageType = messageType;
        this.content = content;
        this.messageId = messageId;
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
        final InboundTextMessage textMessage = new InboundTextMessage(this);
        return new InboundMessageEnvelop(fromUser, toUser, textMessage);
    }

    public String getContent() {
        return content;
    }

    @Override
    public long getMessageId() {
        return messageId;
    }
}
