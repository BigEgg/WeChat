package com.thoughtworks.wechat_io.core;

import org.joda.time.DateTime;

import java.util.Optional;

public class ExpirableResource {
    private String key;
    private String type;
    private String value;
    private DateTime expireTime;

    public ExpirableResource(String key, String type, String value, int expiresInSecond, DateTime createdTime) {
        this.key = key;
        this.type = type;
        this.value = value;
        this.expireTime = createdTime.plusSeconds(expiresInSecond);
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public boolean isExpired() {
        return expireTime.isBeforeNow();
    }

    public Optional<String> getValue() {
        return expireTime.isBeforeNow()
                ? Optional.<String>empty()
                : Optional.of(value);
    }
}
