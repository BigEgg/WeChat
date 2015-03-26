package com.thoughtworks.wechat_application.api.oauth;

import com.thoughtworks.wechat_application.api.APITestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuthRefreshResponseTest extends APITestBase {
    @Test
    public void serializesToJSON() throws Exception {
        final OAuthRefreshResponse oAuthRefreshResponse = new OAuthRefreshResponse("access_token", "refresh_token");
        assertThat(serializeObject(oAuthRefreshResponse))
                .isEqualTo(getResource("fixtures/api/oauth/OAuthResponse.json"));
    }
}