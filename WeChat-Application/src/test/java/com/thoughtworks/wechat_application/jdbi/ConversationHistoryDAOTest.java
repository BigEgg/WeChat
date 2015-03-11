package com.thoughtworks.wechat_application.jdbi;

import com.thoughtworks.wechat_application.core.ConversationHistory;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ConversationHistoryDAOTest extends AbstractDAOTest {
    private final String WORKFLOW_NAME = "Subscribe";
    private ConversationHistoryDAO conversationHistoryDAO;
    private MemberDAO memberDAO;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        conversationHistoryDAO = getDAO(ConversationHistoryDAO.class);
        memberDAO = getDAO(MemberDAO.class);
    }

    @Test
    public void testCreateConversationHistory() throws Exception {
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());

        long historyId = conversationHistoryDAO.createConversationHistory(memberId, WORKFLOW_NAME, getHappenedTime());
        assertThat(historyId, equalTo(1L));
        historyId = conversationHistoryDAO.createConversationHistory(memberId, WORKFLOW_NAME, getHappenedTime());
        assertThat(historyId, equalTo(2L));
    }

    @Test(expected = UnableToExecuteStatementException.class)
    public void testCreateConversationHistory_NoMember() throws Exception {
        conversationHistoryDAO.createConversationHistory(1, WORKFLOW_NAME, getHappenedTime());
    }

    @Test
    public void testGetNotCompleteConversationHistoryByMemberId() throws Exception {
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());
        final long historyId = conversationHistoryDAO.createConversationHistory(memberId, WORKFLOW_NAME, getHappenedTime());

        ConversationHistory history = conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(memberId);
        assertThat(history, notNullValue());
        assertThat(history.getId(), equalTo(historyId));
        assertThat(history.getMemberId(), equalTo(memberId));
        assertThat(history.getStartTime(), notNullValue());
        assertThat(history.getEndTime().isPresent(), equalTo(false));
        assertThat(history.getContent().isPresent(), equalTo(false));
    }

    @Test
    public void testGetNotCompleteConversationHistoryByMemberId_NoHistory() throws Exception {
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());

        conversationHistoryDAO.createConversationHistory(memberId, WORKFLOW_NAME, getHappenedTime());

        ConversationHistory history = conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(2L);
        assertThat(history, nullValue());
    }

    @Test
    public void testGetNotCompleteConversationHistoryByMemberId_WithContent() throws Exception {
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());
        final long historyId = conversationHistoryDAO.createConversationHistory(memberId, WORKFLOW_NAME, getHappenedTime());

        conversationHistoryDAO.updateContentById(historyId, "Content");

        ConversationHistory history = conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(memberId);
        assertThat(history, notNullValue());
        assertThat(history.getMemberId(), equalTo(memberId));
        assertThat(history.getStartTime(), notNullValue());
        assertThat(history.getEndTime().isPresent(), equalTo(false));
        assertThat(history.getContent().isPresent(), equalTo(true));
    }

    @Test
    public void testGetNotCompleteConversationHistoryByMemberId_WithEndTime() throws Exception {
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());
        final long historyId = conversationHistoryDAO.createConversationHistory(memberId, WORKFLOW_NAME, getHappenedTime());

        conversationHistoryDAO.updateEndTimeById(historyId, getHappenedTime());

        ConversationHistory history = conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(memberId);
        assertThat(history, nullValue());
    }

    @Test
    public void testUpdateContentById() throws Exception {
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());
        final long historyId = conversationHistoryDAO.createConversationHistory(memberId, WORKFLOW_NAME, getHappenedTime());

        conversationHistoryDAO.updateContentById(historyId, "Content");

        final ConversationHistory history = conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(memberId);
        assertThat(history, notNullValue());
        assertThat(history.getContent().isPresent(), equalTo(true));
        assertThat(history.getContent().get(), equalTo("Content"));
    }

    @Test
    public void testUpdateEndTimeById() throws Exception {
        final long memberId = memberDAO.createMember("OpenId", getHappenedTime());
        final long historyId = conversationHistoryDAO.createConversationHistory(memberId, WORKFLOW_NAME, getHappenedTime());

        conversationHistoryDAO.updateEndTimeById(historyId, getHappenedTime());

        final ConversationHistory history = conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(memberId);
        assertThat(history, nullValue());
    }
}