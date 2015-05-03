package com.thoughtworks.wechat_application.services.admin;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.jdbi.core.ExpirableResource;
import com.thoughtworks.wechat_application.jdbi.core.TextMessage;
import com.thoughtworks.wechat_application.models.systemMessage.SystemMessage;
import com.thoughtworks.wechat_application.models.systemMessage.TextSystemMessage;
import com.thoughtworks.wechat_application.services.ExpirableResourceService;
import com.thoughtworks.wechat_application.services.TextMessageService;
import com.thoughtworks.wechat_application.utils.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.thoughtworks.wechat_core.util.HashHelper.sha1Hash;

@Singleton
public class AdminResourceService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AdminResourceService.class);
    private final String RESOURCE_TYPE = "Admin";
    private final ExpirableResourceService expirableResourceService;
    private final TextMessageService textMessageService;
    private final CacheManager cacheManager;
    private final WeChatService weChatService = new WeChatService();
    private final SystemMessageService systemMessageService = new SystemMessageService();

    @Inject
    public AdminResourceService(final ExpirableResourceService expirableResourceService,
                                final TextMessageService textMessageService,
                                final CacheManager cacheManager) {
        this.expirableResourceService = expirableResourceService;
        this.textMessageService = textMessageService;
        this.cacheManager = cacheManager;
    }

    public String getResource(final AdminResourceKey key, final String defaultValue) {
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
            LOGGER.warn("[GetResource] Cannot find admin resource: {}. Re-create it with default value {}.", keyString, defaultValue);
            return expirableResourceService.setResource(keyString, RESOURCE_TYPE, defaultValue, 0).getValue().get();
        }
    }

    public void setResource(final AdminResourceKey key, final String value) {
        final String keyString = key.toString();
        LOGGER.info("[SetResource] Set admin resource '{}' to '{}'.", keyString, value);
        cacheManager.expire(keyString);
        expirableResourceService.setResource(keyString, RESOURCE_TYPE, value, 0);
    }

    public String getCachedResources(final AdminResourceKey resourceKey, final String defaultValue) {
        final String keyString = resourceKey.toString();
        final Optional<String> result = cacheManager.get(keyString, String.class);
        if (!result.isPresent()) {
            LOGGER.warn("[GetCachedResources] Cannot find admin resource in cache: {}. Re-create it with default value {}.", keyString, defaultValue);
            final String resource = getResource(resourceKey, defaultValue);
            cacheManager.put(keyString, resource);
        }
        return cacheManager.get(keyString, String.class).get();
    }

    public WeChatService weChat() {
        return weChatService;
    }

    public SystemMessageService systemMessage() {
        return systemMessageService;
    }

    public class WeChatService {
        public String getAppToken() {
            final String appToken = getCachedResources(AdminResourceKey.WECHAT_APP_TOKEN, "");
            if (Objects.equals(appToken, "")) {
                LOGGER.info("[GetAppToken] Don't have App Token now, Create a new one.");
                setResource(AdminResourceKey.WECHAT_APP_TOKEN, createNewAppToken());
            }
            return getCachedResources(AdminResourceKey.WECHAT_APP_TOKEN, "");
        }

        public String getAppSecret() {
            return getCachedResources(AdminResourceKey.WECHAT_APP_SECRET, "");
        }

        public void setAppSecret(final String appSecret) {
            setResource(AdminResourceKey.WECHAT_APP_SECRET, appSecret);
        }

        public String getAppId() {
            return getCachedResources(AdminResourceKey.WECHAT_APP_ID, "");
        }

        public void setAppId(final String appId) {
            setResource(AdminResourceKey.WECHAT_APP_SECRET, appId);
        }

        public boolean getConnectionStatus() {
            return Boolean.parseBoolean(getCachedResources(AdminResourceKey.WECHAT_CONNECTION_STATUS, ""));
        }

        public void setConnectionStatus(final String value) {
            setResource(AdminResourceKey.WECHAT_CONNECTION_STATUS, value);
        }

        public boolean getAPIStatus() {
            return Boolean.parseBoolean(getCachedResources(AdminResourceKey.WECHAT_API_STATUS, ""));
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
    }

    public class SystemMessageService {
        private final String RESOURCE_CONTENT_SPLIT = ":";
        private final String MESSAGE_TYPE_TEXT = "text";

        private final Map<AdminResourceKey, String> keyNameMap = new HashMap<>();

        public SystemMessageService() {
            keyNameMap.clear();
            keyNameMap.put(AdminResourceKey.SUBSCRIBE_RESPONSE, "Subscribe");
            keyNameMap.put(AdminResourceKey.DEFAULT_RESPONSE, "Default");
        }

        public SystemMessage getMessageResource(final AdminResourceKey key) {
            final String content = getResource(key, getDefaultMessageResourceContent(key));
            final String[] strings = content.split(RESOURCE_CONTENT_SPLIT);

            switch (strings[0].toLowerCase()) {
                case MESSAGE_TYPE_TEXT:
                    LOGGER.info("[GetMessageResource] Get text system message ");
                    final String title = strings[1];
                    final TextMessage textMessage = textMessageService.getTextSystemMessageByTitle(title);
                    return new TextSystemMessage(textMessage.getContent());
                default:
                    return new TextSystemMessage("");
            }
        }

        private String getDefaultMessageResourceContent(final AdminResourceKey key) {
            if (keyNameMap.containsKey(key)) {
                return MESSAGE_TYPE_TEXT + RESOURCE_CONTENT_SPLIT + keyNameMap.get(key);
            } else {
                LOGGER.error("[GetMessageDefaultName] Cannot find the default message name for key: {}.", key.toString());
                throw new UnsupportedOperationException();
            }
        }
    }
}
