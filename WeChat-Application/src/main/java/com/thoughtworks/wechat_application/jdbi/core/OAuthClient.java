package com.thoughtworks.wechat_application.jdbi.core;

import java.util.Optional;

public class OAuthClient {
    private final String hashedClientSecret;
    private final AuthenticateRole role;
    private long id;
    private String clientId;
    private Optional<Long> weChatMemberId;

    public OAuthClient(final long id, final String clientId, final String hashedClientSecret, final AuthenticateRole role, final Optional<Long> weChatMemberId) {
        this.id = id;
        this.clientId = clientId;
        this.hashedClientSecret = hashedClientSecret;
        this.role = role;
        this.weChatMemberId = weChatMemberId;
    }

    public long getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getHashedClientSecret() {
        return hashedClientSecret;
    }

    public AuthenticateRole getRole() {
        return role;
    }

    public Optional<Long> getWeChatMemberId() {
        return weChatMemberId;
    }
}
