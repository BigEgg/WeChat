package com.thoughtworks.wechat_application.jdbi.core;

import org.joda.time.DateTime;

import java.util.Optional;

public class ExpirableResource {
    private String key;
    private String type;
    private String value;
    private DateTime expireTime;
    private boolean isNeverExpired;

    public ExpirableResource(String key, String type, String value, int expiresInSecond, DateTime createdTime) {
        this.key = key;
        this.type = type;
        this.value = value;
        this.expireTime = createdTime.plusSeconds(expiresInSecond);

        if (expiresInSecond == 0) {
            isNeverExpired = true;
        }
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public boolean isExpired() {
        return !isNeverExpired && expireTime.isBeforeNow();
    }

    public Optional<String> getValue() {
        return isExpired()
                ? Optional.<String>empty()
                : Optional.of(value);
    }

    public String getOldValue() {
        return value;
    }
}
