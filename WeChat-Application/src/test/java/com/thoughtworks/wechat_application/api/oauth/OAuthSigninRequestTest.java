package com.thoughtworks.wechat_application.api.oauth;

import com.thoughtworks.wechat_application.api.APIModelTestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuthSignInRequestTest extends APIModelTestBase {
    @Test
    public void deserializeFromJSON() throws Exception {
        final OAuthSignInRequest request = deserializeFixture("fixtures/uas/oauth/OAuthSignInRequest.json", OAuthSignInRequest.class);
        assertThat(request.getClientId()).isEqualTo("abc@abc.com");
        assertThat(request.getClientSecret()).isEqualTo("password");
    }
}