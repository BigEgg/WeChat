package com.thoughtworks.wechat_application.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.jdbi.MemberDAO;
import com.thoughtworks.wechat_application.jdbi.core.Member;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceTest {
    @Mock
    private MemberDAO memberDAO;
    private MemberService service;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(binder -> {
            binder.bind(MemberDAO.class).toInstance(memberDAO);
        });

        service = injector.getInstance(MemberService.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final MemberService anotherService = injector.getInstance(MemberService.class);
        assertThat(service, equalTo(anotherService));
    }

    @Test
    public void testGetMemberByOpenId_Exist() throws Exception {
        when(memberDAO.getMemberByOpenId("openId")).thenReturn(createUnsubscribeMember());

        final Optional<Member> member = service.getMemberByOpenId("openId");

        verify(memberDAO).getMemberByOpenId(eq("openId"));
        assertThat(member.isPresent(), equalTo(true));
        assertThat(member.get().getId(), equalTo(1L));
        assertThat(member.get().getWeChatOpenId(), equalTo("openId"));
        assertThat(member.get().isSubscribed(), equalTo(false));
    }

    @Test
    public void testGetMemberByOpenId_NotExist() throws Exception {
        when(memberDAO.getMemberByOpenId("openId")).thenReturn(null);

        final Optional<Member> member = service.getMemberByOpenId("openId");

        verify(memberDAO).getMemberByOpenId(eq("openId"));
        assertThat(member.isPresent(), equalTo(false));
    }

    @Test
    public void testSubscribeMember_NotExist() throws Exception {
        final Member mockMember = createUnsubscribeMember();
        when(memberDAO.getMemberByOpenId("openId")).thenReturn(null, mockMember);
        when(memberDAO.createMember(eq("openId"), any(Timestamp.class))).thenReturn(mockMember.getId());

        final Member member = service.subscribeMember("openId");

        verify(memberDAO, times(2)).getMemberByOpenId(eq("openId"));
        verify(memberDAO).createMember(eq("openId"), any(Timestamp.class));
        verify(memberDAO, never()).updateSubscribed(anyLong(), anyBoolean(), any(Timestamp.class));
        assertThat(member, notNullValue());
        assertThat(member.getId(), equalTo(1L));
        assertThat(member.getWeChatOpenId(), equalTo("openId"));
        assertThat(member.isSubscribed(), equalTo(false));
    }

    @Test
    public void testSubscribeMember_Exist() throws Exception {
        when(memberDAO.getMemberByOpenId("openId")).thenReturn(createUnsubscribeMember(), createSubscribeMember());

        final Member member = service.subscribeMember("openId");

        verify(memberDAO, times(2)).getMemberByOpenId(eq("openId"));
        verify(memberDAO).updateSubscribed(eq(1L), eq(true), any(Timestamp.class));
        verify(memberDAO, never()).createMember(anyString(), any(Timestamp.class));
        assertThat(member, notNullValue());
        assertThat(member.getId(), equalTo(1L));
        assertThat(member.getWeChatOpenId(), equalTo("openId"));
        assertThat(member.isSubscribed(), equalTo(true));
    }

    @Test
    public void testUnsubscribeMember_Exist() throws Exception {
        when(memberDAO.getMemberByOpenId("openId")).thenReturn(createSubscribeMember());

        service.unsubscribeMember("openId");

        verify(memberDAO).getMemberByOpenId(eq("openId"));
        verify(memberDAO).updateSubscribed(eq(1L), eq(false), any(Timestamp.class));
    }

    @Test
    public void testUnsubscribeMember_NotExist() throws Exception {
        when(memberDAO.getMemberByOpenId("openId")).thenReturn(null);

        service.unsubscribeMember("openId");

        verify(memberDAO).getMemberByOpenId(eq("openId"));
        verify(memberDAO, never()).updateSubscribed(anyLong(), anyBoolean(), any(Timestamp.class));
    }

    private Member createUnsubscribeMember() {
        return new Member(1L, "openId", false);
    }

    private Member createSubscribeMember() {
        return new Member(1L, "openId", true);
    }
}