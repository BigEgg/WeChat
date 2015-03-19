package com.thoughtworks.wechat_application.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.core.ExpirableResource;
import com.thoughtworks.wechat_application.jdbi.ExpirableResourceDAO;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExpirableResourceServiceTest {
    @Mock
    private ExpirableResourceDAO expirableResourceDAO;
    private ExpirableResourceService service;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(binder -> {
            binder.bind(ExpirableResourceDAO.class).toInstance(expirableResourceDAO);
        });

        service = injector.getInstance(ExpirableResourceService.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final ExpirableResourceService anotherExpirableResourceService = injector.getInstance(ExpirableResourceService.class);
        assertThat(service, equalTo(anotherExpirableResourceService));
    }

    @Test
    public void testSetResource_NotExist() throws Exception {
        when(expirableResourceDAO.getResource("key", "type")).thenReturn(null, createExpirableResource1());

        final ExpirableResource expirableResource = service.setResource("key", "type", "value", 10);

        verify(expirableResourceDAO, times(2)).getResource(eq("key"), eq("type"));
        verify(expirableResourceDAO).createResource(eq("key"), eq("type"), eq("value"), eq(10), any(Timestamp.class));
        verify(expirableResourceDAO, never()).updateResource(anyString(), anyString(), anyString(), anyInt(), any(Timestamp.class));
        assertThat(expirableResource, notNullValue());
        assertThat(expirableResource.getKey(), equalTo("key"));
        assertThat(expirableResource.getType(), equalTo("type"));
        assertThat(expirableResource.isExpired(), equalTo(false));
        assertThat(expirableResource.getValue().isPresent(), equalTo(true));
        assertThat(expirableResource.getValue().get(), equalTo("value1"));
    }

    @Test
    public void testSetResource_Exist() throws Exception {
        when(expirableResourceDAO.getResource("key", "type")).thenReturn(createExpirableResource1(), createExpirableResource2());

        final ExpirableResource expirableResource = service.setResource("key", "type", "value2", 10);

        verify(expirableResourceDAO, times(2)).getResource(eq("key"), eq("type"));
        verify(expirableResourceDAO).updateResource(eq("key"), eq("type"), eq("value2"), eq(10), any(Timestamp.class));
        verify(expirableResourceDAO, never()).createResource(anyString(), anyString(), anyString(), anyInt(), any(Timestamp.class));
        assertThat(expirableResource, notNullValue());
        assertThat(expirableResource.getKey(), equalTo("key"));
        assertThat(expirableResource.getType(), equalTo("type"));
        assertThat(expirableResource.isExpired(), equalTo(false));
        assertThat(expirableResource.getValue().isPresent(), equalTo(true));
        assertThat(expirableResource.getValue().get(), equalTo("value2"));
    }

    @Test
    public void testGetResource_Exist() throws Exception {
        final ExpirableResource newExpirableResource = createExpirableResource1();
        when(expirableResourceDAO.getResource("key", "type")).thenReturn(newExpirableResource);

        final Optional<ExpirableResource> resource = service.getResource("key", "type");

        verify(expirableResourceDAO).getResource("key", "type");
        assertThat(resource.isPresent(), equalTo(true));
        assertThat(resource.get(), equalTo(newExpirableResource));
    }

    @Test
    public void testGetResource_NotExist() throws Exception {
        when(expirableResourceDAO.getResource("key", "type")).thenReturn(null);

        final Optional<ExpirableResource> resource = service.getResource("key", "type");

        verify(expirableResourceDAO).getResource("key", "type");
        assertThat(resource.isPresent(), equalTo(false));
    }

    @Test
    public void testDeleteResources_Exist() throws Exception {
        when(expirableResourceDAO.getResource("key", "type")).thenReturn(createExpirableResource1());

        service.deleteResources("key", "type");

        verify(expirableResourceDAO).getResource(eq("key"), eq("type"));
        verify(expirableResourceDAO).deleteResources(eq("key"), eq("type"));
    }

    @Test
    public void testDeleteResources_NotExist() throws Exception {
        when(expirableResourceDAO.getResource("key", "type")).thenReturn(null);

        service.deleteResources("key", "type");

        verify(expirableResourceDAO).getResource(eq("key"), eq("type"));
        verify(expirableResourceDAO, never()).deleteResources(anyString(), anyString());
    }

    private ExpirableResource createExpirableResource1() {
        return new ExpirableResource("key", "type", "value1", 10, DateTime.now());
    }

    private ExpirableResource createExpirableResource2() {
        return new ExpirableResource("key", "type", "value2", 10, DateTime.now());
    }
}