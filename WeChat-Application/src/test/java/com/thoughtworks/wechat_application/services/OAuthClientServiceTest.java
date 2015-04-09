package com.thoughtworks.wechat_application.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.jdbi.OAuthClientDAO;
import com.thoughtworks.wechat_application.jdbi.core.AuthenticateRole;
import com.thoughtworks.wechat_application.jdbi.core.Member;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import com.thoughtworks.wechat_application.services.admin.passwordUtils.PasswordHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OAuthClientServiceTest {
    @Mock
    private OAuthClientDAO oAuthClientDAO;
    @Mock
    private PasswordHelper passwordHelper;
    private OAuthClientService service;
    private Injector injector;

    @Before
    public void setUp() {
        injector = Guice.createInjector(binder -> {
            binder.bind(OAuthClientDAO.class).toInstance(oAuthClientDAO);
            binder.bind(PasswordHelper.class).toInstance(passwordHelper);
        });

        service = injector.getInstance(OAuthClientService.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final OAuthClientService anotherService = injector.getInstance(OAuthClientService.class);
        assertThat(service, equalTo(anotherService));
    }

    @Test
    public void testSignIn() throws Exception {
        when(oAuthClientDAO.getByClientId("clientId")).thenReturn(createAdmin());
        when(passwordHelper.getSaltFromHashedPassword("hashedClientSecret")).thenReturn("salt");
        when(passwordHelper.saltHash("clientSecret", "salt")).thenReturn("hashedClientSecret");

        final Optional<OAuthClient> adminUser = service.SignIn("clientId", "clientSecret");

        verify(oAuthClientDAO).getByClientId(eq("clientId"));
        verify(passwordHelper).getSaltFromHashedPassword(eq("hashedClientSecret"));
        verify(passwordHelper).saltHash(eq("clientSecret"), eq("salt"));
        assertThat(adminUser.isPresent(), equalTo(true));
        assertThat(adminUser.get().getId(), equalTo(1L));
        assertThat(adminUser.get().getClientId(), equalTo("clientId"));
        assertThat(adminUser.get().getHashedClientSecret(), equalTo("hashedClientSecret"));
        assertThat(adminUser.get().getWeChatMemberId().isPresent(), equalTo(false));
    }

    @Test
    public void testSignIn_UserNotExist() throws Exception {
        when(oAuthClientDAO.getByClientId("clientId")).thenReturn(null);

        final Optional<OAuthClient> adminUser = service.SignIn("clientId", "clientSecret");

        verify(oAuthClientDAO).getByClientId(eq("clientId"));
        verify(passwordHelper, never()).getSaltFromHashedPassword(anyString());
        verify(passwordHelper, never()).saltHash(anyString(), anyString());
        assertThat(adminUser.isPresent(), equalTo(false));
    }

    @Test
    public void testSignIn_PasswordNotRight() throws Exception {
        when(oAuthClientDAO.getByClientId("clientId")).thenReturn(createAdmin());
        when(passwordHelper.getSaltFromHashedPassword("hashedClientSecret")).thenReturn("salt");
        when(passwordHelper.saltHash("clientSecret", "salt")).thenReturn("hashedClientSecret123");

        final Optional<OAuthClient> adminUser = service.SignIn("clientId", "clientSecret");

        verify(oAuthClientDAO).getByClientId(eq("clientId"));
        verify(passwordHelper).getSaltFromHashedPassword(eq("hashedClientSecret"));
        verify(passwordHelper).saltHash(eq("clientSecret"), eq("salt"));
        assertThat(adminUser.isPresent(), equalTo(false));
    }

    @Test
    public void testCreateAdmin() throws Exception {
        when(oAuthClientDAO.getByClientId("clientId")).thenReturn(null, createAdmin());
        when(passwordHelper.saltHash("clientSecret")).thenReturn("hashedClientSecret");

        final Optional<OAuthClient> adminUser = service.createAdmin("clientId", "clientSecret");

        verify(oAuthClientDAO).create(eq("clientId"), eq("hashedClientSecret"), eq(AuthenticateRole.ADMIN));
        verify(oAuthClientDAO, times(2)).getByClientId(eq("clientId"));
        assertThat(adminUser.isPresent(), equalTo(true));
        assertThat(adminUser.get().getId(), equalTo(1L));
        assertThat(adminUser.get().getClientId(), equalTo("clientId"));
        assertThat(adminUser.get().getHashedClientSecret(), equalTo("hashedClientSecret"));
        assertThat(adminUser.get().getRole(), equalTo(AuthenticateRole.ADMIN));
        assertThat(adminUser.get().getWeChatMemberId().isPresent(), equalTo(false));
    }

    @Test
    public void testCreateAdmin_SameUsername() throws Exception {
        when(oAuthClientDAO.getByClientId("clientId")).thenReturn(createAdmin());

        final Optional<OAuthClient> adminUser = service.createAdmin("clientId", "password1");

        verify(oAuthClientDAO, never()).create(anyString(), anyString(), anyObject());
        verify(oAuthClientDAO).getByClientId(eq("clientId"));
        verify(passwordHelper, never()).saltHash(eq("clientSecret"));
        assertThat(adminUser.isPresent(), equalTo(false));
    }

    @Test
    public void testSetMember() throws Exception {
        final OAuthClient OAuthClient = createAdmin();
        service.setMember(OAuthClient, createSubscribeMember());

        verify(oAuthClientDAO).setMember(eq("clientId"), eq(1L));
    }

    private OAuthClient createAdmin() {
        return new OAuthClient(1L, "clientId", "hashedClientSecret", AuthenticateRole.ADMIN, Optional.<Long>empty());
    }

    private OAuthClient createVendor() {
        return new OAuthClient(1L, "clientId2", "hashedClientSecret2", AuthenticateRole.VENDOR, Optional.<Long>empty());
    }

    private Member createSubscribeMember() {
        return new Member(1L, "openId", true);
    }
}