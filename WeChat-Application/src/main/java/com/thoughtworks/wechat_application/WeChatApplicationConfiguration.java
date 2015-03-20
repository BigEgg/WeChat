package com.thoughtworks.wechat_application;

import com.thoughtworks.wechat_application.configs.CacheConfiguration;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

public class WeChatApplicationConfiguration extends Configuration {
    @NotNull
    private CacheConfiguration cacheConfiguration;

    public CacheConfiguration getCacheConfiguration() {
        return cacheConfiguration;
    }
}
