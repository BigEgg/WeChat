package com.thoughtworks.wechat_application.api.oauth;

import com.thoughtworks.wechat_application.api.APIModelTestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuthResponseTest extends APIModelTestBase {
    @Test
    public void serializeToJSON_WhenSuccess() throws Exception {
        final OAuthResponse OAuthResponse = new OAuthResponse("abcdefgh", "abcdefgh12345678");
        assertThat(serializeObject(OAuthResponse))
                .isEqualTo(getResource("fixtures/uas/oauth/OAuthResponse_Success.json"));
    }

    @Test
    public void serializeToJSON_WhenFailed() throws Exception {
        final OAuthResponse OAuthResponse = new OAuthResponse();
        assertThat(serializeObject(OAuthResponse))
                .isEqualTo(getResource("fixtures/uas/oauth/OAuthResponse_Failed.json"));
    }
}