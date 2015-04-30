package com.thoughtworks.wechat_application.services.admin;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.jdbi.core.ExpirableResource;
import com.thoughtworks.wechat_application.jdbi.core.TextMessage;
import com.thoughtworks.wechat_application.services.ExpirableResourceService;
import com.thoughtworks.wechat_application.services.TextMessageService;
import com.thoughtworks.wechat_application.utils.CacheManager;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_core.messages.outbound.messages.OutboundTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.thoughtworks.wechat_core.util.HashHelper.sha1Hash;

@Singleton
public class AdminResourceService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AdminResourceService.class);
    private final String RESOURCE_TYPE = "Admin";
    private final ExpirableResourceService expirableResourceService;
    private final TextMessageService textMessageService;
    private final CacheManager cacheManager;

    @Inject
    public AdminResourceService(final ExpirableResourceService expirableResourceService,
                                final TextMessageService textMessageService,
                                final CacheManager cacheManager) {
        this.expirableResourceService = expirableResourceService;
        this.textMessageService = textMessageService;
        this.cacheManager = cacheManager;
    }

    public String getAppToken() {
        final String appToken = getCachedResources(AdminResourceKey.WECHAT_APP_TOKEN);
        if (Objects.equals(appToken, "")) {
            LOGGER.info("[GetAppToken] Don't have App Token now, Create a new one.");
            setResource(AdminResourceKey.WECHAT_APP_TOKEN, createNewAppToken());
        }
        return getCachedResources(AdminResourceKey.WECHAT_APP_TOKEN);
    }

    public String getAppSecret() {
        return getCachedResources(AdminResourceKey.WECHAT_APP_SECRET);
    }

    public void setAppSecret(final String appSecret) {
        cacheManager.expire(AdminResourceKey.WECHAT_APP_SECRET.toString());
        setResource(AdminResourceKey.WECHAT_APP_SECRET, appSecret);
    }

    public String getAppId() {
        return getCachedResources(AdminResourceKey.WECHAT_APP_ID);
    }

    public void setAppId(final String appId) {
        cacheManager.expire(AdminResourceKey.WECHAT_APP_SECRET.toString());
        setResource(AdminResourceKey.WECHAT_APP_SECRET, appId);
    }

    public String getResource(final AdminResourceKey key) {
        final String keyString = key.toString();
        LOGGER.info("[GetResource] Try get admin resource: {}.", keyString);
        final Optional<ExpirableResource> resource = expirableResourceService.getResource(keyString, RESOURCE_TYPE);
        if (resource.isPresent()) {
            final ExpirableResource expirableResource = resource.get();
            if (!expirableResource.isExpired()) {
                final String value = expirableResource.getValue().get();
                LOGGER.info("[GetResource] Get the admin resource: {}, value: {}.", keyString, value);
                return value;
            } else {
                LOGGER.warn("[GetResource] Can get the admin resource: {}, but somehow it expired. Reset the expire time.", keyString);
                return expirableResourceService.setResource(keyString, RESOURCE_TYPE, expirableResource.getOldValue(), 0).getValue().get();
            }
        } else {
            LOGGER.warn("[GetResource] Cannot find admin resource: {}. Re-create it.", keyString);
            return expirableResourceService.setResource(keyString, RESOURCE_TYPE, "", 0).getValue().get();
        }
    }

    public void setResource(final AdminResourceKey key, final String value) {
        expirableResourceService.setResource(key.toString(), RESOURCE_TYPE, value, 0);
    }

    public Optional<OutboundMessage> getMessageResource(final AdminResourceKey key) {
        final String content = getResource(key);
        if (content.equals("")) {
            return Optional.empty();
        }

        final String[] strings = content.toLowerCase().split(":");
        switch (strings[0]) {
            case "text":
                final Optional<TextMessage> textMessage = textMessageService.getTextMessageByTitle(strings[1]);
                if (textMessage.isPresent()) {
                    return Optional.of(new OutboundTextMessage(textMessage.get().getContent()));
                } else {
                    return Optional.empty();
                }
            default:
                return Optional.empty();
        }
    }

    private String getCachedResources(AdminResourceKey resourceKey) {
        final Optional<String> result = cacheManager.get(resourceKey.toString(), String.class);
        if (!result.isPresent()) {
            final String resource = getResource(resourceKey);
            cacheManager.put(resourceKey.toString(), resource);
        }
        return cacheManager.get(resourceKey.toString(), String.class).get();
    }

    private String createNewAppToken() {
        final int APP_TOKEN_LENGTH = 16;
        final String uuid = UUID.randomUUID().toString();
        try {
            return sha1Hash(uuid).substring(0, APP_TOKEN_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            return uuid.replace("-", "").substring(0, APP_TOKEN_LENGTH);
        }
    }

    public boolean getWeChatConnectionStatus() {
        return Boolean.parseBoolean(getCachedResources(AdminResourceKey.WECHAT_CONNECTION_STATUS));
    }

    public boolean getWeChatAPIStatus() {
        return Boolean.parseBoolean(getCachedResources(AdminResourceKey.WECHAT_API_STATUS));
    }
}
