package com.thoughtworks.wechat_application.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.core.ConversationHistory;
import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_application.jdbi.ConversationHistoryDAO;
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
public class ConversationHistoryServiceTest {
    @Mock
    private ConversationHistoryDAO conversationHistoryDAO;
    private ConversationHistoryService service;

    @Before
    public void setUp() throws Exception {
        service = new ConversationHistoryService(conversationHistoryDAO);
    }

    @Test
    public void testInjection() throws Exception {
        final Injector injector = Guice.createInjector(binder -> {
            binder.bind(ConversationHistoryDAO.class).toInstance(conversationHistoryDAO);
        });

        final ConversationHistoryService conversationHistoryService = injector.getInstance(ConversationHistoryService.class);
        assertThat(conversationHistoryService, notNullValue());
    }

    @Test
    public void testInjection_Singleton() throws Exception {
        final Injector injector = Guice.createInjector(binder -> {
            binder.bind(ConversationHistoryDAO.class).toInstance(conversationHistoryDAO);
        });

        final ConversationHistoryService conversationHistoryService = injector.getInstance(ConversationHistoryService.class);
        final ConversationHistoryService anotherConversationHistoryService = injector.getInstance(ConversationHistoryService.class);
        assertThat(conversationHistoryService, equalTo(anotherConversationHistoryService));
    }

    @Test
    public void testStartNewConversation_NoActiveConversation() throws Exception {
        final Member member = createSubscribeMember();
        when(conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(member.getId())).thenReturn(null, createConversationHistory1());

        final ConversationHistory conversation = service.startNewConversation(member, "Subscribe");

        verify(conversationHistoryDAO, times(2)).getNotCompleteConversationHistoryByMemberId(eq(member.getId()));
        verify(conversationHistoryDAO, never()).updateEndTimeById(anyLong(), any(Timestamp.class));
        verify(conversationHistoryDAO).createConversationHistory(eq(member.getId()), eq("Subscribe"), any(Timestamp.class));

        assertThat(conversation, notNullValue());
        assertThat(conversation.getId(), equalTo(1L));
        assertThat(conversation.getMemberId(), equalTo(1L));
        assertThat(conversation.getWorkflowName(), equalTo("Subscribe"));
        assertThat(conversation.getStartTime(), notNullValue());
        assertThat(conversation.getEndTime().isPresent(), equalTo(false));
        assertThat(conversation.getContent().isPresent(), equalTo(false));
    }

    @Test
    public void testStartNewConversation_HaveActiveConversation() throws Exception {
        final Member member = createSubscribeMember();
        when(conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(member.getId())).thenReturn(createConversationHistory1(), createConversationHistory2());

        final ConversationHistory conversation = service.startNewConversation(member, "Subscribe");

        verify(conversationHistoryDAO, times(2)).getNotCompleteConversationHistoryByMemberId(eq(member.getId()));
        verify(conversationHistoryDAO).updateEndTimeById(eq(1L), any(Timestamp.class));
        verify(conversationHistoryDAO).createConversationHistory(eq(member.getId()), eq("Subscribe"), any(Timestamp.class));

        assertThat(conversation, notNullValue());
        assertThat(conversation.getId(), equalTo(2L));
        assertThat(conversation.getMemberId(), equalTo(1L));
        assertThat(conversation.getWorkflowName(), equalTo("Subscribe"));
        assertThat(conversation.getStartTime(), notNullValue());
        assertThat(conversation.getEndTime().isPresent(), equalTo(false));
        assertThat(conversation.getContent().isPresent(), equalTo(false));
    }

    @Test
    public void testGetMemberConversation_NoActiveConversation() throws Exception {
        final Member member = createSubscribeMember();
        when(conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(member.getId())).thenReturn(null);

        final Optional<ConversationHistory> conversation = service.getMemberConversation(member);

        verify(conversationHistoryDAO).getNotCompleteConversationHistoryByMemberId(eq(member.getId()));
        assertThat(conversation.isPresent(), equalTo(false));
    }

    @Test
    public void testGetMemberConversation_HaveActiveConversation() throws Exception {
        final Member member = createSubscribeMember();
        when(conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(member.getId())).thenReturn(createConversationHistory1());

        final Optional<ConversationHistory> conversation = service.getMemberConversation(member);

        verify(conversationHistoryDAO).getNotCompleteConversationHistoryByMemberId(eq(member.getId()));
        assertThat(conversation.isPresent(), equalTo(true));
    }

    @Test
    public void testEndConversation_NoActiveConversation() throws Exception {
        final Member member = createSubscribeMember();
        when(conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(member.getId())).thenReturn(null);

        service.endConversation(member);

        verify(conversationHistoryDAO).getNotCompleteConversationHistoryByMemberId(eq(member.getId()));
        verify(conversationHistoryDAO, never()).updateEndTimeById(anyLong(), any(Timestamp.class));
    }

    @Test
    public void testEndConversation_HaveActiveConversation() throws Exception {
        final Member member = createSubscribeMember();
        when(conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(member.getId())).thenReturn(createConversationHistory1());

        service.endConversation(member);

        verify(conversationHistoryDAO).getNotCompleteConversationHistoryByMemberId(eq(member.getId()));
        verify(conversationHistoryDAO).updateEndTimeById(eq(1L), any(Timestamp.class));
    }

    @Test
    public void testUpdateConversationContent_NoActiveConversation() throws Exception {
        final Member member = createSubscribeMember();
        when(conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(member.getId())).thenReturn(null);

        service.updateConversationContent(member, "NewContent");

        verify(conversationHistoryDAO).getNotCompleteConversationHistoryByMemberId(eq(member.getId()));
        verify(conversationHistoryDAO, never()).updateContentById(anyLong(), anyString());
    }

    @Test
    public void testUpdateConversationContent_HaveActiveConversation() throws Exception {
        final Member member = createSubscribeMember();
        when(conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(member.getId())).thenReturn(createConversationHistory1());

        service.updateConversationContent(member, "NewContent");

        verify(conversationHistoryDAO).getNotCompleteConversationHistoryByMemberId(eq(member.getId()));
        verify(conversationHistoryDAO).updateContentById(eq(1L), eq("NewContent"));
    }

    private Member createSubscribeMember() {
        return new Member(1L, "openId", true);
    }

    private ConversationHistory createConversationHistory1() {
        return new ConversationHistory(1L, 1L, "Subscribe", DateTime.now(), Optional.<DateTime>empty(), Optional.<String>empty());
    }

    private ConversationHistory createConversationHistory2() {
        return new ConversationHistory(2L, 1L, "Subscribe", DateTime.now(), Optional.<DateTime>empty(), Optional.<String>empty());
    }
}