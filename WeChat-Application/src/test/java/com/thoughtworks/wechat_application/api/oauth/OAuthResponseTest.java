package com.thoughtworks.wechat_application.api.oauth;

import com.thoughtworks.wechat_application.api.APITestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuthResponseTest extends APITestBase {
    @Test
    public void serializeToJSON() throws Exception {
        final OAuthResponse OAuthResponse = new OAuthResponse("abcdefgh", "abcdefgh12345678");
        assertThat(serializeObject(OAuthResponse))
                .isEqualTo(getResource("fixtures/uas/oauth/OAuthResponse.json"));
    }
}