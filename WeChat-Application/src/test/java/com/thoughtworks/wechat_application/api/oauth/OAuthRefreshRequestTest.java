package com.thoughtworks.wechat_application.api.oauth;

import com.thoughtworks.wechat_application.api.APITestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuthRefreshRequestTest extends APITestBase {
    @Test
    public void deserializeFromJson() throws Exception {
        final OAuthRefreshRequest oAuthRefreshRequest = new OAuthRefreshRequest();
        oAuthRefreshRequest.setAccessToken("access_token");
        oAuthRefreshRequest.setRefreshToken("refresh_token");

        final OAuthRefreshRequest request = deserializeFixture("fixtures/api/oauth/OAuthRefreshRequest.json", OAuthRefreshRequest.class);
        assertThat(request.getAccessToken()).isEqualTo(oAuthRefreshRequest.getAccessToken());
        assertThat(request.getRefreshToken()).isEqualTo(oAuthRefreshRequest.getRefreshToken());
    }
}