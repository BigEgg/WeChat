package com.thoughtworks.wechat_application.services.admin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.jdbi.core.ExpirableResource;
import com.thoughtworks.wechat_application.jdbi.core.TextMessage;
import com.thoughtworks.wechat_application.models.systemMessage.SystemMessage;
import com.thoughtworks.wechat_application.models.systemMessage.TextSystemMessage;
import com.thoughtworks.wechat_application.services.ExpirableResourceService;
import com.thoughtworks.wechat_application.services.TextMessageService;
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
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(binder -> {
            binder.bind(ExpirableResourceService.class).toInstance(expirableResourceService);
            binder.bind(TextMessageService.class).toInstance(textMessageService);
        });

        service = injector.getInstance(AdminResourceService.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final AdminResourceService anotherAdminResourceService = injector.getInstance(AdminResourceService.class);
        assertThat(service, equalTo(anotherAdminResourceService));
    }

    @Test
    public void testGetResource_NotExist() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.<ExpirableResource>empty());
        when(expirableResourceService.setResource("SUBSCRIBE_RESPONSE", "Admin", "defaultValue", 0)).thenReturn(createNeverExpiredResource(""));

        final String resource = service.getResource(AdminResourceKey.SUBSCRIBE_RESPONSE, "defaultValue");

        verify(expirableResourceService).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(expirableResourceService).setResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"), eq("defaultValue"), eq(0));
        assertThat(resource, equalTo(""));
    }

    @Test
    public void testGetResource_Expired() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.of(createExpiredResource("content")));
        when(expirableResourceService.setResource("SUBSCRIBE_RESPONSE", "Admin", "content", 0)).thenReturn(createNeverExpiredResource("content"));

        final String resource = service.getResource(AdminResourceKey.SUBSCRIBE_RESPONSE, "defaultValue");

        verify(expirableResourceService).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(expirableResourceService).setResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"), eq("content"), eq(0));
        assertThat(resource, equalTo("content"));
    }

    @Test
    public void testGetResource_Exist() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.of(createNeverExpiredResource("content")));

        final String resource = service.getResource(AdminResourceKey.SUBSCRIBE_RESPONSE, "defaultValue");

        verify(expirableResourceService).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(expirableResourceService, never()).setResource(anyString(), anyString(), anyString(), anyInt());
        assertThat(resource, equalTo("content"));
    }

    @Test
    public void testGetMessageResource_ResourceNotExist() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.<ExpirableResource>empty());
        when(expirableResourceService.setResource("SUBSCRIBE_RESPONSE", "Admin", "text:Subscribe", 0)).thenReturn(createNeverExpiredResource("text:Subscribe"));
        when(textMessageService.getTextSystemMessageByTitle("Subscribe")).thenReturn(createTextMessage("Admin_Subscribe"));

        SystemMessage message = service.systemMessage().getMessageResource(AdminResourceKey.SUBSCRIBE_RESPONSE);

        verify(expirableResourceService).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(expirableResourceService).setResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"), eq("text:Subscribe"), eq(0));
        verify(textMessageService).getTextSystemMessageByTitle(eq("Subscribe"));
        assertThat(message, notNullValue());
        assertThat(message instanceof TextSystemMessage, equalTo(true));
    }

    @Test
    public void testGetMessageResource_ResourceExpired() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.of(createExpiredResource("text:Subscribe")));
        when(expirableResourceService.setResource("SUBSCRIBE_RESPONSE", "Admin", "text:Subscribe", 0)).thenReturn(createNeverExpiredResource("text:Subscribe"));
        when(textMessageService.getTextSystemMessageByTitle("Subscribe")).thenReturn(createTextMessage("Admin_Subscribe"));

        SystemMessage message = service.systemMessage().getMessageResource(AdminResourceKey.SUBSCRIBE_RESPONSE);

        verify(expirableResourceService).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(expirableResourceService).setResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"), eq("text:Subscribe"), eq(0));
        verify(textMessageService).getTextSystemMessageByTitle(eq("Subscribe"));
        assertThat(message, notNullValue());
        assertThat(message instanceof TextSystemMessage, equalTo(true));
    }

    @Test
    public void testGetMessageResource_NoMessage() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.of(createNeverExpiredResource("text:Subscribe")));
        when(textMessageService.getTextSystemMessageByTitle("Subscribe")).thenReturn(createTextMessage("Admin_Subscribe"));

        SystemMessage message = service.systemMessage().getMessageResource(AdminResourceKey.SUBSCRIBE_RESPONSE);

        verify(expirableResourceService).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        assertThat(message, notNullValue());
        assertThat(message instanceof TextSystemMessage, equalTo(true));
    }

    @Test
    public void testGetMessageResource_TextMessage() throws Exception {
        when(expirableResourceService.getResource("SUBSCRIBE_RESPONSE", "Admin")).thenReturn(Optional.of(createNeverExpiredResource("text:Admin_Subscribe")));
        when(textMessageService.getTextSystemMessageByTitle("Admin_Subscribe")).thenReturn(createTextMessage("Admin_Subscribe"));

        SystemMessage message = service.systemMessage().getMessageResource(AdminResourceKey.SUBSCRIBE_RESPONSE);

        verify(expirableResourceService).getResource(eq("SUBSCRIBE_RESPONSE"), eq("Admin"));
        verify(textMessageService).getTextSystemMessageByTitle(eq("Admin_Subscribe"));
        assertThat(message, notNullValue());
        assertThat(message instanceof TextSystemMessage, equalTo(true));
    }

    private ExpirableResource createNeverExpiredResource(final String content) {
        return new ExpirableResource("SUBSCRIBE_RESPONSE", "Admin", content, 0, DateTime.now());
    }

    private ExpirableResource createExpiredResource(final String content) {
        return new ExpirableResource("SUBSCRIBE_RESPONSE", "Admin", content, 1, DateTime.now().minusHours(1));
    }

    private TextMessage createTextMessage(final String title) {
        return new TextMessage(1L, title, "Content");
    }
}