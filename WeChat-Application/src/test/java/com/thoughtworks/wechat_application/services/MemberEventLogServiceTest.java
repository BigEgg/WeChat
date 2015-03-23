package com.thoughtworks.wechat_application.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.jdbi.core.Member;
import com.thoughtworks.wechat_application.jdbi.EventLogDAO;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MemberEventLogServiceTest {
    @Mock
    private EventLogDAO eventLogDAO;
    private EventLogService service;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(binder -> {
            binder.bind(EventLogDAO.class).toInstance(eventLogDAO);
        });

        service = injector.getInstance(EventLogService.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final EventLogService anotherService = injector.getInstance(EventLogService.class);
        assertThat(service, equalTo(anotherService));
    }

    @Test
    public void testLogSubscribe() throws Exception {
        service.member().subscribe(createSubscribeMember(), DateTime.now());

        verify(eventLogDAO).insertEventLog(eq(1L), eq("Member"), eq("Subscribe"), any(Timestamp.class));
    }

    @Test
    public void testLogUnsubscribe() throws Exception {
        service.member().unsubscribe(createUnsubscribeMember(), DateTime.now());

        verify(eventLogDAO).insertEventLog(eq(1L), eq("Member"), eq("Unsubscribe"), any(Timestamp.class));
    }

    private Member createUnsubscribeMember() {
        return new Member(1L, "openId", false);
    }

    private Member createSubscribeMember() {
        return new Member(1L, "openId", true);
    }
}