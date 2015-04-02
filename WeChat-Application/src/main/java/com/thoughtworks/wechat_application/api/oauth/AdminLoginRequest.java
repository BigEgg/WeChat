package com.thoughtworks.wechat_application.api.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class AdminLoginRequest {
    @NotBlank
    @Length(max = 32)
    @JsonProperty("username")
    private String username;
    @NotBlank
    @Length(max = 32)
    @JsonProperty("password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
