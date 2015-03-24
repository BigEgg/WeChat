package com.thoughtworks.wechat_application.logic;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.configs.OAuthConfiguration;
import com.thoughtworks.wechat_application.models.oauth.AuthenticateRole;
import com.thoughtworks.wechat_application.models.oauth.OAuthInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OAuthProviderTest {
    @Mock
    private OAuthConfiguration configuration;
    private OAuthProvider oAuthProvider;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        when(configuration.getoAuthAccessTokenLength()).thenReturn(8);
        when(configuration.getoAuthRefreshTokenLength()).thenReturn(16);

        injector = Guice.createInjector(binder -> {
            binder.bind(OAuthConfiguration.class).toInstance(configuration);
        });

        oAuthProvider = injector.getInstance(OAuthProvider.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final OAuthProvider anotherProvider = injector.getInstance(OAuthProvider.class);
        assertThat(anotherProvider, equalTo(oAuthProvider));
    }

    @Test
    public void testNewOAuth() throws Exception {
        when(configuration.getoAuthAccessTokenExpireSeconds()).thenReturn(10);
        when(configuration.getoAuthRefreshTokenExpireSeconds()).thenReturn(10);

        final OAuthInfo oAuthInfo = oAuthProvider.newOAuth(AuthenticateRole.ADMIN);

        assertThat(oAuthInfo.getRole(), equalTo(AuthenticateRole.ADMIN));
        assertThat(oAuthInfo.getAccessToken().isPresent(), equalTo(true));
        assertThat(oAuthInfo.getAccessToken().get(), any(String.class));
        assertThat(oAuthInfo.getAccessToken().get().length(), equalTo(configuration.getoAuthAccessTokenLength()));
        assertThat(oAuthInfo.getRefreshToken().isPresent(), equalTo(true));
        assertThat(oAuthInfo.getRefreshToken().get(), any(String.class));
        assertThat(oAuthInfo.getRefreshToken().get().length(), equalTo(configuration.getoAuthRefreshTokenLength()));
    }

    @Test
    public void testRefreshAccessToken() throws Exception {
        when(configuration.getoAuthAccessTokenExpireSeconds()).thenReturn(1, 10);
        when(configuration.getoAuthRefreshTokenExpireSeconds()).thenReturn(10);

        final OAuthInfo oAuthInfo = oAuthProvider.newOAuth(AuthenticateRole.ADMIN);
        final String accessToken = oAuthInfo.getAccessToken().get();
        Thread.sleep(1000);

        final Optional<OAuthInfo> newOAuthInfoOpt = oAuthProvider.refreshAccessToken(accessToken, oAuthInfo.getRefreshToken().get());
        assertThat(newOAuthInfoOpt.isPresent(), equalTo(true));

        final OAuthInfo newOAuthInfo = newOAuthInfoOpt.get();
        assertThat(newOAuthInfo.getRole(), equalTo(AuthenticateRole.ADMIN));
        assertThat(newOAuthInfo.getAccessToken().isPresent(), equalTo(true));
        assertThat(newOAuthInfo.getAccessToken().get(), any(String.class));
        assertThat(newOAuthInfo.getAccessToken().get().length(), equalTo(configuration.getoAuthAccessTokenLength()));
        assertThat(newOAuthInfo.getRefreshToken().isPresent(), equalTo(true));
        assertThat(newOAuthInfo.getRefreshToken().get(), any(String.class));
        assertThat(newOAuthInfo.getRefreshToken().get().length(), equalTo(configuration.getoAuthRefreshTokenLength()));
    }

    @Test
    public void testRefreshAccessToken_NoMatchAccessToken() throws Exception {
        when(configuration.getoAuthAccessTokenExpireSeconds()).thenReturn(1);
        when(configuration.getoAuthRefreshTokenExpireSeconds()).thenReturn(10);

        final OAuthInfo oAuthInfo = oAuthProvider.newOAuth(AuthenticateRole.ADMIN);
        Thread.sleep(1000);

        final Optional<OAuthInfo> newOAuthInfoOpt = oAuthProvider.refreshAccessToken("wrongAccessToken", oAuthInfo.getRefreshToken().get());
        assertThat(newOAuthInfoOpt.isPresent(), equalTo(false));
    }

    @Test
    public void testRefreshAccessToken_RefreshTokenExpired() throws Exception {
        when(configuration.getoAuthAccessTokenExpireSeconds()).thenReturn(10);
        when(configuration.getoAuthRefreshTokenExpireSeconds()).thenReturn(1);

        final OAuthInfo oAuthInfo = oAuthProvider.newOAuth(AuthenticateRole.ADMIN);
        final String accessToken = oAuthInfo.getAccessToken().get();
        final String refreshToken = oAuthInfo.getRefreshToken().get();
        Thread.sleep(1000);

        final Optional<OAuthInfo> newOAuthInfoOpt = oAuthProvider.refreshAccessToken(accessToken, refreshToken);
        assertThat(newOAuthInfoOpt.isPresent(), equalTo(false));
    }

    @Test
    public void testGetRole() throws Exception {
        when(configuration.getoAuthAccessTokenExpireSeconds()).thenReturn(10);
        when(configuration.getoAuthRefreshTokenExpireSeconds()).thenReturn(10);

        final OAuthInfo oAuthInfo = oAuthProvider.newOAuth(AuthenticateRole.ADMIN);
        final AuthenticateRole role = oAuthProvider.getRole(oAuthInfo.getAccessToken().get());

        assertThat(role, equalTo(AuthenticateRole.ADMIN));
    }

    @Test
    public void testGetRole_AccessTokenExpired() throws Exception {
        when(configuration.getoAuthAccessTokenExpireSeconds()).thenReturn(1);
        when(configuration.getoAuthRefreshTokenExpireSeconds()).thenReturn(10);

        final OAuthInfo oAuthInfo = oAuthProvider.newOAuth(AuthenticateRole.ADMIN);
        final String accessToken = oAuthInfo.getAccessToken().get();
        Thread.sleep(1000);

        final AuthenticateRole role = oAuthProvider.getRole(accessToken);
        assertThat(role, equalTo(AuthenticateRole.NONE));
    }

    @Test
    public void testGetRole_NoMatchAccessToken() throws Exception {
        when(configuration.getoAuthAccessTokenExpireSeconds()).thenReturn(10);
        when(configuration.getoAuthRefreshTokenExpireSeconds()).thenReturn(10);

        oAuthProvider.newOAuth(AuthenticateRole.ADMIN);
        final AuthenticateRole role = oAuthProvider.getRole("wrongAccessToken");

        assertThat(role, equalTo(AuthenticateRole.NONE));
    }
}