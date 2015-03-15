package com.thoughtworks.wechat_application.services.admin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.core.ExpirableResource;
import com.thoughtworks.wechat_application.core.TextMessage;
import com.thoughtworks.wechat_application.services.ExpirableResourceService;
import com.thoughtworks.wechat_application.services.TextMessageService;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_core.messages.outbound.messages.OutboundTextMessage;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminResourceServiceTest {
    @Mock
    private ExpirableResourceService expirableResourceService;
    @Mock
    private TextMessageService textMessageService;
    private AdminResourceService service;

    @Before
    public void setUp() throws Exception {
        service = new AdminResourceService(expirableResourceService, textMessageService);
    }

    @Test
    public void testInject() throws Exception {
        final Injector injector = Guice.createInjector(binder -> {
            binder.bind(ExpirableResourceService.class).toInstance(expirableResourceService);
            binder.bind(TextMessageService.class).toInstance(textMessageService);
        });

        final AdminResourceService adminResourceService = injector.getInstance(AdminResourceService.class);
        assertThat(adminResourceService, notNullValue());
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final Injector injector = Guice.createInjector(binder -> {
            binder.bind(ExpirableResourceService.class).toInstance(expirableResourceService);
            binder.bind(TextMessageService.class).toInstance(textMessageService);
        });

        final AdminResourceService adminResourceService = injector.getInstance(AdminResourceService.class);
        final AdminResourceService anotherAdminResourceService = injector.getInstance(AdminResourceService.class);
        assertThat(adminResourceService, equalTo(anotherAdminResourceService));
    }

    @Test
    public void testGetResource_NotExist() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.<ExpirableResource>empty());
        when(expirableResourceService.setResource("SUBSCRIBE_RESPONSE", "Admin", "", 0)).thenReturn(createNeverExpiredResource(""));

        final String resource = service.getResource(AdminResourceKeys.SUBSCRIBE_RESPONSE);

        verify(expirableResourceService, times(1)).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(expirableResourceService, times(1)).setResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"), eq(""), eq(0));
        assertThat(resource, equalTo(""));
    }

    @Test
    public void testGetResource_Expired() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.of(createExpiredResource("content")));
        when(expirableResourceService.setResource("SUBSCRIBE_RESPONSE", "Admin", "content", 0)).thenReturn(createNeverExpiredResource("content"));

        final String resource = service.getResource(AdminResourceKeys.SUBSCRIBE_RESPONSE);

        verify(expirableResourceService, times(1)).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(expirableResourceService, times(1)).setResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"), eq("content"), eq(0));
        assertThat(resource, equalTo("content"));
    }

    @Test
    public void testGetResource_Exist() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.of(createNeverExpiredResource("content")));

        final String resource = service.getResource(AdminResourceKeys.SUBSCRIBE_RESPONSE);

        verify(expirableResourceService, times(1)).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(expirableResourceService, never()).setResource(anyString(), anyString(), anyString(), anyInt());
        assertThat(resource, equalTo("content"));
    }

    @Test
    public void testGetMessageResource_ResourceNotExist() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.<ExpirableResource>empty());
        when(expirableResourceService.setResource("SUBSCRIBE_RESPONSE", "Admin", "", 0)).thenReturn(createNeverExpiredResource(""));

        final Optional<OutboundMessage> message = service.getMessageResource(AdminResourceKeys.SUBSCRIBE_RESPONSE);

        verify(expirableResourceService, times(1)).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(expirableResourceService, times(1)).setResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"), eq(""), eq(0));
        verify(textMessageService, never()).getTextMessageByTitle(anyString());
        assertThat(message.isPresent(), equalTo(false));
    }

    @Test
    public void testGetMessageResource_ResourceExpired() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.of(createExpiredResource("content")));
        when(expirableResourceService.setResource("SUBSCRIBE_RESPONSE", "Admin", "content", 0)).thenReturn(createNeverExpiredResource("content"));

        final Optional<OutboundMessage> message = service.getMessageResource(AdminResourceKeys.SUBSCRIBE_RESPONSE);

        verify(expirableResourceService, times(1)).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(expirableResourceService, times(1)).setResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"), eq("content"), eq(0));
        verify(textMessageService, never()).getTextMessageByTitle(anyString());
        assertThat(message.isPresent(), equalTo(false));
    }

    @Test
    public void testGetMessageResource_NotMessage() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.of(createNeverExpiredResource("content")));

        final Optional<OutboundMessage> message = service.getMessageResource(AdminResourceKeys.SUBSCRIBE_RESPONSE);

        verify(expirableResourceService, times(1)).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(expirableResourceService, never()).setResource(anyString(), anyString(), anyString(), anyInt());
        verify(textMessageService, never()).getTextMessageByTitle(anyString());
        assertThat(message.isPresent(), equalTo(false));
    }

    @Test
    public void testGetMessageResource_TextMessage() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.of(createNeverExpiredResource("text:title")));
        when(textMessageService.getTextMessageByTitle("title")).thenReturn(Optional.of(createTextMessage()));

        final Optional<OutboundMessage> message = service.getMessageResource(AdminResourceKeys.SUBSCRIBE_RESPONSE);

        verify(expirableResourceService, times(1)).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(expirableResourceService, never()).setResource(anyString(), anyString(), anyString(), anyInt());
        verify(textMessageService, times(1)).getTextMessageByTitle("title");
        assertThat(message.isPresent(), equalTo(true));
        assertThat(message.get() instanceof OutboundTextMessage, equalTo(true));
    }

    private ExpirableResource createNeverExpiredResource(String content) {
        return new ExpirableResource("SUBSCRIBE_RESPONSE", "Admin", content, 0, DateTime.now());
    }

    private ExpirableResource createExpirableResource(String content) {
        return new ExpirableResource("SUBSCRIBE_RESPONSE", "Admin", content, 1, DateTime.now());
    }

    private ExpirableResource createExpiredResource(String content) {
        return new ExpirableResource("SUBSCRIBE_RESPONSE", "Admin", content, 1, DateTime.now().minusHours(1));
    }

    private TextMessage createTextMessage() {
        return new TextMessage(1L, "title", "Content");
    }
}