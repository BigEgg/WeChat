package com.thoughtworks.wechat_application.jdbi.core;

public class Label {
    private long id;
    private String title;

    public Label(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
