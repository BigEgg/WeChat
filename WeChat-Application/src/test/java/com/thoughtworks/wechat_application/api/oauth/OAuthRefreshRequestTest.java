package com.thoughtworks.wechat_application.api.oauth;

import com.thoughtworks.wechat_application.api.APIModelTestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuthRefreshRequestTest extends APIModelTestBase {
    @Test
    public void deserializeFromJSON() throws Exception {
        final OAuthRefreshRequest request = deserializeFixture("fixtures/uas/oauth/OAuthRefreshRequest.json", OAuthRefreshRequest.class);
        assertThat(request.getAccessToken()).isEqualTo("access_token");
        assertThat(request.getRefreshToken()).isEqualTo("refresh_token");
    }
}