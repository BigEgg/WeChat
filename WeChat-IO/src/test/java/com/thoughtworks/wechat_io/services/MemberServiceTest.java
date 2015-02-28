package com.thoughtworks.wechat_io.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_io.core.Label;
import com.thoughtworks.wechat_io.core.Member;
import com.thoughtworks.wechat_io.jdbi.MemberDAO;
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
    private MemberService memberService;

    @Before
    public void setUp() throws Exception {
        memberService = new MemberService(memberDAO, labelService);
    }

    @Test
    public void testInject() {
        Injector injector = Guice.createInjector(binder -> {
            binder.bind(MemberDAO.class).toInstance(memberDAO);
            binder.bind(LabelService.class).toInstance(labelService);
        });

        MemberService service = injector.getInstance(MemberService.class);
        assertThat(service, notNullValue());
    }

    @Test
    public void testInject_Singleton() {
        Injector injector = Guice.createInjector(binder -> {
            binder.bind(MemberDAO.class).toInstance(memberDAO);
            binder.bind(LabelService.class).toInstance(labelService);
        });

        MemberService service = injector.getInstance(MemberService.class);
        MemberService anotherService = injector.getInstance(MemberService.class);
        assertThat(service, equalTo(anotherService));
    }

    @Test
    public void testGetMemberByOpenId_Exist() throws Exception {
        when(memberDAO.getMemberByOpenId("openId")).thenReturn(createUnsubscribeMember());

        Optional<Member> member = memberService.getMemberByOpenId("openId");

        verify(memberDAO, times(1)).getMemberByOpenId(eq("openId"));
        assertThat(member.isPresent(), equalTo(true));
        assertThat(member.get().getId(), equalTo(1L));
        assertThat(member.get().getWeChatOpenId(), equalTo("openId"));
        assertThat(member.get().isSubscribed(), equalTo(false));
    }

    @Test
    public void testGetMemberByOpenId_NotExist() throws Exception {
        when(memberDAO.getMemberByOpenId("openId")).thenReturn(null);

        Optional<Member> member = memberService.getMemberByOpenId("openId");

        verify(memberDAO, times(1)).getMemberByOpenId(eq("openId"));
        assertThat(member.isPresent(), equalTo(false));
    }

    @Test
    public void testSubscribeMember_NotExist() throws Exception {
        Member mockMember = createUnsubscribeMember();
        when(memberDAO.getMemberByOpenId("openId")).thenReturn(null, mockMember);
        when(memberDAO.createMember(eq("openId"), any(Timestamp.class))).thenReturn(mockMember.getId());

        Member member = memberService.subscribeMember("openId");

        verify(memberDAO, times(2)).getMemberByOpenId(eq("openId"));
        verify(memberDAO, times(1)).createMember(eq("openId"), any(Timestamp.class));
        verify(memberDAO, never()).updateSubscribed(anyLong(), anyBoolean());
        assertThat(member, notNullValue());
        assertThat(member.getId(), equalTo(1L));
        assertThat(member.getWeChatOpenId(), equalTo("openId"));
        assertThat(member.isSubscribed(), equalTo(false));
    }

    @Test
    public void testSubscribeMember_Exist() throws Exception {
        when(memberDAO.getMemberByOpenId("openId")).thenReturn(createUnsubscribeMember(), createSubscribeMember());

        Member member = memberService.subscribeMember("openId");

        verify(memberDAO, times(2)).getMemberByOpenId(eq("openId"));
        verify(memberDAO, times(1)).updateSubscribed(eq(1L), eq(true));
        verify(memberDAO, never()).createMember(anyString(), any(Timestamp.class));
        assertThat(member, notNullValue());
        assertThat(member.getId(), equalTo(1L));
        assertThat(member.getWeChatOpenId(), equalTo("openId"));
        assertThat(member.isSubscribed(), equalTo(true));
    }

    @Test
    public void testUnsubscribeMember_Exist() throws Exception {
        when(memberDAO.getMemberByOpenId("openId")).thenReturn(createSubscribeMember());

        memberService.unsubscribeMember("openId");

        verify(memberDAO, times(1)).getMemberByOpenId(eq("openId"));
        verify(memberDAO, times(1)).updateSubscribed(eq(1L), eq(false));
    }

    @Test
    public void testUnsubscribeMember_NotExist() throws Exception {
        when(memberDAO.getMemberByOpenId("openId")).thenReturn(null);

        memberService.unsubscribeMember("openId");

        verify(memberDAO, times(1)).getMemberByOpenId(eq("openId"));
        verify(memberDAO, never()).updateSubscribed(anyLong(), anyBoolean());
    }

    @Test
    public void testLinkMemberToLabel_NotLinked() throws Exception {
        Member member = createSubscribeMember();
        Label label = createLabel1();
        when(labelService.getMemberLabels(member)).thenReturn(Optional.empty());

        memberService.linkMemberToLabel(member, label);

        verify(labelService, times(1)).getMemberLabels(eq(member));
        verify(memberDAO, times(1)).linkMemberWithLabel(eq(member.getId()), eq(label.getId()));
        verify(memberDAO, never()).updateMemberLabel(anyLong(), anyLong());
    }

    @Test
    public void testLinkMemberToLabel_Linked() throws Exception {
        Member member = createSubscribeMember();
        when(labelService.getMemberLabels(member)).thenReturn(Optional.of(createLabel1()));

        memberService.linkMemberToLabel(member, createLabel2());

        verify(labelService, times(1)).getMemberLabels(eq(member));
        verify(memberDAO, times(1)).updateMemberLabel(eq(member.getId()), eq(2L));
        verify(memberDAO, never()).linkMemberWithLabel(anyLong(), anyLong());
    }

    @Test
    public void testDelinkMemberLabel_Linked() throws Exception {
        Member member = createSubscribeMember();
        when(labelService.getMemberLabels(member)).thenReturn(Optional.of(createLabel1()));

        memberService.delinkMemberLabel(member);

        verify(labelService, times(1)).getMemberLabels(eq(member));
        verify(memberDAO, times(1)).delinkMemberWithLabel(eq(member.getId()), eq(1L));
    }

    @Test
    public void testDelinkMemberLabel_NotLinked() throws Exception {
        Member member = createSubscribeMember();
        when(labelService.getMemberLabels(member)).thenReturn(Optional.empty());

        memberService.delinkMemberLabel(member);

        verify(labelService, times(1)).getMemberLabels(eq(member));
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