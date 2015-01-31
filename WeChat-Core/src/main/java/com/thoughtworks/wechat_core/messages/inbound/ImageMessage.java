package com.thoughtworks.wechat_core.messages.inbound;

public class ImageMessage implements InboundMessage {
    private InboundMessageType messageType;
    private int createdTime;
    private long messageId;
    private String pictureUrl;
    private String mediaId;

    @Override
    public InboundMessageType getMessageType() {
        return messageType;
    }

    @Override
    public int getCreatedTime() {
        return createdTime;
    }

    @Override
    public long getMessageId() {
        return messageId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getMediaId() {
        return mediaId;
    }
}
