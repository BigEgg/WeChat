package com.thoughtworks.wechat_application;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.thoughtworks.wechat_application.configs.CacheConfiguration;

public class WeChatApplicationModule implements Module {
    private final CacheConfiguration cacheConfiguration;

    public WeChatApplicationModule(final CacheConfiguration cacheConfiguration) {
        this.cacheConfiguration = cacheConfiguration;
    }

    @Override
    public void configure(final Binder binder) {
        binder.bind(CacheConfiguration.class).toInstance(cacheConfiguration);
    }
}
