package com.thoughtworks.wechat_application.core;

import java.util.Optional;

public class AdminUser {
    private long id;
    private String username;
    private String hashedPassword;
    private Optional<Long> memberId;

    public AdminUser(long id, String username, String hashedPassword, Optional<Long> memberId) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.memberId = memberId;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public Optional<Long> getMemberId() {
        return memberId;
    }
}
