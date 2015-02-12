package com.thoughtworks.wechat_io.core;

public class Label {
    private long id;
    private String name;

    public Label(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
