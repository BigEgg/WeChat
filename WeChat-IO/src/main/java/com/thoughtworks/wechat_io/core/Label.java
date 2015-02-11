package com.thoughtworks.wechat_io.core;

public class Label {
    private long Id;
    private String name;

    public Label(long id, String name) {
        Id = id;
        this.name = name;
    }

    public long getId() {
        return Id;
    }

    public String getName() {
        return name;
    }
}
