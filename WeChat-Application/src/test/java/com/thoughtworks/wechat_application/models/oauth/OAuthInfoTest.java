package com.thoughtworks.wechat_application.models.oauth;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


public class OAuthInfoTest {
    @Test
    public void testGenerate() throws Exception {
        final OAuthInfo oAuthInfo = new OAuthInfo(AuthenticateRole.ADMIN, "accessToken", "refreshToken", 10, 10, DateTime.now());

        assertThat(oAuthInfo.getRole(), equalTo(AuthenticateRole.ADMIN));
        assertThat(oAuthInfo.getAccessToken().isPresent(), equalTo(true));
        assertThat(oAuthInfo.getAccessToken().get(), equalTo("accessToken"));
        assertThat(oAuthInfo.getRefreshToken().isPresent(), equalTo(true));
        assertThat(oAuthInfo.getRefreshToken().get(), equalTo("refreshToken"));
    }

    @Test
    public void testAccessTokenExpired() throws Exception {
        final OAuthInfo oAuthInfo = new OAuthInfo(AuthenticateRole.ADMIN, "accessToken", "refreshToken", 1, 10, DateTime.now());

        Thread.sleep(1000);
        assertThat(oAuthInfo.getRole(), equalTo(AuthenticateRole.ADMIN));
        assertThat(oAuthInfo.getAccessToken().isPresent(), equalTo(false));
        assertThat(oAuthInfo.getRefreshToken().isPresent(), equalTo(true));
        assertThat(oAuthInfo.getRefreshToken().get(), equalTo("refreshToken"));
    }

    @Test
    public void testRefreshTokenExpired() throws Exception {
        final OAuthInfo oAuthInfo = new OAuthInfo(AuthenticateRole.ADMIN, "accessToken", "refreshToken", 10, 1, DateTime.now());

        Thread.sleep(1000);
        assertThat(oAuthInfo.getRole(), equalTo(AuthenticateRole.ADMIN));
        assertThat(oAuthInfo.getAccessToken().isPresent(), equalTo(false));
        assertThat(oAuthInfo.getRefreshToken().isPresent(), equalTo(false));
    }
}