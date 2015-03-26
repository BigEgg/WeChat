package com.thoughtworks.wechat_application.api.oauth;

import com.thoughtworks.wechat_application.api.APITestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminLoginResponseTest extends APITestBase {
    @Test
    public void serializeToJSON() throws Exception {
        final AdminLoginResponse adminLoginResponse = new AdminLoginResponse("abcdefgh", "abcdefgh12345678", "abc");
        assertThat(serializeObject(adminLoginResponse))
                .isEqualTo(getResource("fixtures/api/oauth/AdminLoginResponse.json"));
    }
}