package com.thoughtworks.wechat_application.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.core.ExpirableResource;
import com.thoughtworks.wechat_application.jdbi.ExpirableResourceDAO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUnixTimestamp;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

@Singleton
public class ExpirableResourceService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExpirableResourceService.class);
    private final ExpirableResourceDAO expirableResourceDAO;

    @Inject
    public ExpirableResourceService(final ExpirableResourceDAO expirableResourceDAO) {
        this.expirableResourceDAO = expirableResourceDAO;
    }

    public ExpirableResource setResource(final String key, final String type, final String value, final int expiresInSecond) {
        checkNotBlank(key);
        checkNotBlank(type);
        checkNotBlank(value);
        checkArgument(expiresInSecond >= 0);

        LOGGER.info("[SetResource] Try set an expirable resource(key: {}, type: {}).", key, type);
        final ExpirableResource resource = expirableResourceDAO.getResource(key, type);
        if (resource == null) {
            LOGGER.info("[SetResource] No expirable resource(key: {}, type: {}) found. Create a new one with value: {}, expires in '{}' second(s).", key, type, value, expiresInSecond);
            expirableResourceDAO.createResource(key, type, value, expiresInSecond, toUnixTimestamp(DateTime.now()), toUnixTimestamp(DateTime.now()));
        } else {
            LOGGER.info("[SetResource] Found one expirable resource(key: {}, type: {}). Update the value from '{}' to '{}', expires in '{}' second(s).", key, type, resource.getValue(), value, expiresInSecond);
            expirableResourceDAO.updateResource(key, type, value, expiresInSecond, toUnixTimestamp(DateTime.now()));
        }
        return expirableResourceDAO.getResource(key, type);
    }

    public Optional<ExpirableResource> getResource(final String key, final String type) {
        checkNotBlank(key);
        checkNotBlank(type);

        Optional<ExpirableResource> resource = Optional.ofNullable(expirableResourceDAO.getResource(key, type));
        LOGGER.info("[GetResource] Try get expirable resource(key: {}, type: {}). Status: {}.", key, type, resource.isPresent());
        return resource;
    }

    public void deleteResources(final String key, final String type) {
        checkNotBlank(key);
        checkNotBlank(type);

        LOGGER.info("[DeleteResources] Try delete an expirable resource(key: {}, type: {}).", key, type);
        final ExpirableResource resource = expirableResourceDAO.getResource(key, type);
        if (resource != null) {
            LOGGER.info("[DeleteResources] Found one expirable resource(key: {}, type: {}), delete it.", key, type);
            expirableResourceDAO.deleteResources(key, type);
        } else {
            LOGGER.info("[DeleteResources] No expirable resource(key: {}, type: {}) found.", key, type);
        }
    }
}
