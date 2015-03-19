package com.thoughtworks.wechat_application.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.core.Label;
import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_application.jdbi.MemberDAO;
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
    @Mock
    private LabelService labelService;
    private MemberService service;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(binder -> {
            binder.bind(MemberDAO.class).toInstance(memberDAO);
            binder.bind(LabelService.class).toInstance(labelService);
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

    @Test
    public void testLinkMemberToLabel_NotLinked() throws Exception {
        final Member member = createSubscribeMember();
        final Label label = createLabel1();
        when(labelService.getMemberLabels(member)).thenReturn(Optional.empty());

        service.linkMemberToLabel(member, label);

        verify(labelService).getMemberLabels(eq(member));
        verify(memberDAO).linkMemberWithLabel(eq(member.getId()), eq(label.getId()), any(Timestamp.class));
        verify(memberDAO, never()).updateMemberLabel(anyLong(), anyLong(), any(Timestamp.class));
    }

    @Test
    public void testLinkMemberToLabel_Linked() throws Exception {
        final Member member = createSubscribeMember();
        when(labelService.getMemberLabels(member)).thenReturn(Optional.of(createLabel1()));

        service.linkMemberToLabel(member, createLabel2());

        verify(labelService).getMemberLabels(eq(member));
        verify(memberDAO).updateMemberLabel(eq(member.getId()), eq(2L), any(Timestamp.class));
        verify(memberDAO, never()).linkMemberWithLabel(anyLong(), anyLong(), any(Timestamp.class));
    }

    @Test
    public void testDelinkMemberLabel_Linked() throws Exception {
        final Member member = createSubscribeMember();
        when(labelService.getMemberLabels(member)).thenReturn(Optional.of(createLabel1()));

        service.delinkMemberLabel(member);

        verify(labelService).getMemberLabels(eq(member));
        verify(memberDAO).delinkMemberWithLabel(eq(member.getId()), eq(1L));
    }

    @Test
    public void testDelinkMemberLabel_NotLinked() throws Exception {
        final Member member = createSubscribeMember();
        when(labelService.getMemberLabels(member)).thenReturn(Optional.empty());

        service.delinkMemberLabel(member);

        verify(labelService).getMemberLabels(eq(member));
        verify(memberDAO, never()).delinkMemberWithLabel(anyLong(), anyLong());
    }

    private Member createUnsubscribeMember() {
        return new Member(1L, "openId", false);
    }

    private Member createSubscribeMember() {
        return new Member(1L, "openId", true);
    }

    private Label createLabel1() {
        return new Label(1, "Label1");
    }

    private Label createLabel2() {
        return new Label(2, "Label2");
    }
}