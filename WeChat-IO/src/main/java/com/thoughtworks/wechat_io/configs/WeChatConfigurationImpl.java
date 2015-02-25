package com.thoughtworks.wechat_io.configs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Singleton;
import org.hibernate.validator.constraints.NotEmpty;

@Singleton
public class WeChatConfigurationImpl implements WeChatConfiguration {
    @JsonProperty("app_secret")
    @NotEmpty
    private String appSecret;
    @JsonProperty("app_token")
    @NotEmpty
    private String appToken;

    public String getAppSecret() {
        return appSecret;
    }

    public String getAppToken() {
        return appToken;
    }
}
