package com.thoughtworks.wechat_application.api.oauth;

import com.thoughtworks.wechat_application.api.APITestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminLoginRequestTest extends APITestBase {
    @Test
    public void deserializeFromJSON() throws Exception {
        final AdminLoginRequest request = new AdminLoginRequest();
        request.setUsername("abc@abc.com");
        request.setPassword("password");

        final AdminLoginRequest adminLoginRequest = deserializeFixture("fixtures/api/oauth/AdminLoginRequest.json", AdminLoginRequest.class);
        assertThat(request.getUsername()).isEqualTo(adminLoginRequest.getUsername());
        assertThat(request.getPassword()).isEqualTo(adminLoginRequest.getPassword());
    }
}