package com.thoughtworks.wechat_application.services.admin;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.core.ExpirableResource;
import com.thoughtworks.wechat_application.core.TextMessage;
import com.thoughtworks.wechat_application.services.ExpirableResourceService;
import com.thoughtworks.wechat_application.services.TextMessageService;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_core.messages.outbound.messages.OutboundTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Singleton
public class AdminResourceService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AdminResourceService.class);
    private final String RESOURCE_TYPE = "Admin";
    private final ExpirableResourceService expirableResourceService;
    private final TextMessageService textMessageService;

    @Inject
    public AdminResourceService(final ExpirableResourceService expirableResourceService,
                                final TextMessageService textMessageService) {
        this.expirableResourceService = expirableResourceService;
        this.textMessageService = textMessageService;
    }

    public String getResource(final AdminResourceKeys key) {
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

    public Optional<OutboundMessage> getMessageResource(final AdminResourceKeys key) {
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
}
