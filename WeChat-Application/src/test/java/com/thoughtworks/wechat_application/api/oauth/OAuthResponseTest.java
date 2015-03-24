package com.thoughtworks.wechat_application.api.oauth;

import com.thoughtworks.wechat_application.api.APITestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuthResponseTest extends APITestBase {
    @Test
    public void serializesToJSON() throws Exception {
        final OAuthResponse oAuthResponse = new OAuthResponse("access_token", "refresh_token");
        assertThat(serializeObject(oAuthResponse))
                .isEqualTo(getResource("fixtures/api/oauth/OAuthResponse.json"));
    }
}