package com.thoughtworks.wechat_application.services.systemEventLog;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.jdbi.SystemEventLogDAO;
import com.thoughtworks.wechat_application.jdbi.core.AuthenticateRole;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import com.thoughtworks.wechat_application.services.SystemEventLogService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OAuthSystemEventLogServiceTest {
    @Mock
    private SystemEventLogDAO systemEventLogDAO;
    private SystemEventLogService service;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(binder -> {
            binder.bind(SystemEventLogDAO.class).toInstance(systemEventLogDAO);
        });

        service = injector.getInstance(SystemEventLogService.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final SystemEventLogService anotherService = injector.getInstance(SystemEventLogService.class);
        assertThat(service, equalTo(anotherService));
    }

    @Test
    public void testLogGetAccessToken() throws Exception {
        service.oAuth().accessToken(createOAuthClient(), DateTime.now());

        verify(systemEventLogDAO).insertEventLog(eq(1L), eq("OAuth"), eq("GetAccessToken"), any(Timestamp.class));
    }

    @Test
    public void testLogRefreshAccessToken() throws Exception {
        service.oAuth().refresh(createOAuthClient(), DateTime.now());

        verify(systemEventLogDAO).insertEventLog(eq(1L), eq("OAuth"), eq("RefreshAccessToken"), any(Timestamp.class));
    }

    @Test
    public void testLogSignOut() throws Exception {
        service.oAuth().signOut(createOAuthClient(), DateTime.now());

        verify(systemEventLogDAO).insertEventLog(eq(1L), eq("OAuth"), eq("SignOut"), any(Timestamp.class));
    }

    private OAuthClient createOAuthClient() {
        return new OAuthClient(1, "clientId", "hashedClientSecret", AuthenticateRole.ADMIN, Optional.<Long>empty());
    }
}