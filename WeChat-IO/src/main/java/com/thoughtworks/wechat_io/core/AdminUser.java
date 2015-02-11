package com.thoughtworks.wechat_io.core;

import java.util.Optional;

public class AdminUser {
    private long id;
    private String username;
    private String encryptedPassword;
    private Optional<Long> memberId;

    public AdminUser(long id, String username, String encryptedPassword, Optional<Long> memberId) {
        this.id = id;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.memberId = memberId;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public Optional<Long> getMemberId() {
        return memberId;
    }
}
