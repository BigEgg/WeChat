package com.thoughtworks.wechat_io.configs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class CacheConfigurationImpl implements CacheConfiguration {
    @JsonProperty("label")
    @NotEmpty
    private int labelCacheSeconds;

    @Override
    public int getLabelCacheSeconds() {
        return labelCacheSeconds;
    }
}
