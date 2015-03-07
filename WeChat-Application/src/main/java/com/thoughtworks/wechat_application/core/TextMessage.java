package com.thoughtworks.wechat_application.core;

public class TextMessage {
    private long id;
    private String title;
    private String content;

    public TextMessage(long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
