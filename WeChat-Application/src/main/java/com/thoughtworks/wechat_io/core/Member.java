package com.thoughtworks.wechat_io.core;

public class Member {
    private long id;
    private String weChatOpenId;
    private boolean subscribed;

    public Member(long id, String weChatOpenId, boolean subscribed) {
        this.id = id;
        this.weChatOpenId = weChatOpenId;
        this.subscribed = subscribed;
    }

    public long getId() {
        return id;
    }

    public String getWeChatOpenId() {
        return weChatOpenId;
    }

    public boolean isSubscribed() {
        return subscribed;
    }
}
