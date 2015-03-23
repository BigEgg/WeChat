package com.thoughtworks.wechat_application.configs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Singleton;
import org.hibernate.validator.constraints.NotEmpty;

@Singleton
public class CacheConfiguration {
    @NotEmpty
    private int labelCacheSeconds;

    @JsonProperty("label")
    public int getLabelCacheSeconds() {
        return labelCacheSeconds;
    }
}