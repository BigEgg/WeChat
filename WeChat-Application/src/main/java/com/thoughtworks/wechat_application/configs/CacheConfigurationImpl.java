package com.thoughtworks.wechat_application.configs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Singleton;
import org.hibernate.validator.constraints.NotEmpty;

@Singleton
public class CacheConfigurationImpl implements CacheConfiguration {
    @JsonProperty("label")
    @NotEmpty
    private int labelCacheSeconds;

    @Override
    public int getLabelCacheSeconds() {
        return labelCacheSeconds;
    }
}
