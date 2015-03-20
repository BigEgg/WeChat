package com.thoughtworks.wechat_application;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.wechat_application.configs.CacheConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class WeChatApplicationConfiguration extends Configuration {
    @NotNull
    private CacheConfiguration cacheConfiguration;

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    public CacheConfiguration getCacheConfiguration() {
        return cacheConfiguration;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

}
