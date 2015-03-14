package com.thoughtworks.wechat_application.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.core.ConversationHistory;
import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_application.jdbi.ConversationHistoryDAO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.thoughtworks.wechat_core.util.DateTimeExtension.toUnixTimestamp;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

@Singleton
public class ConversationHistoryService {
    private final Logger LOGGER = LoggerFactory.getLogger(ConversationHistoryService.class);
    private final ConversationHistoryDAO conversationHistoryDAO;

    @Inject
    public ConversationHistoryService(final ConversationHistoryDAO conversationHistoryDAO) {
        this.conversationHistoryDAO = conversationHistoryDAO;
    }

    public ConversationHistory startNewConversation(final Member member, final String workflowName) {
        checkNotNull(member);
        checkNotBlank(workflowName);

        final long memberId = member.getId();
        LOGGER.info("[StartNewConversation] Try start a new conversation history for member(id: {}).", memberId);
        final ConversationHistory conversationHistory = conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(memberId);
        if (conversationHistory != null) {
            LOGGER.info("[StartNewConversation] Member(id: {}) already have an active conversation history(id: {}). Close it.");
            conversationHistoryDAO.updateEndTimeById(conversationHistory.getId(), toUnixTimestamp(DateTime.now()));
        }
        final long conversationHistoryId = conversationHistoryDAO.createConversationHistory(memberId, workflowName, toUnixTimestamp(DateTime.now()));
        LOGGER.info("[StartNewConversation] Create a conversation history(id: {}) to member(id: {}).", conversationHistoryId, memberId);
        return conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(memberId);
    }

    public Optional<ConversationHistory> getMemberConversation(final Member member) {
        checkNotNull(member);

        final Optional<ConversationHistory> conversationHistory = Optional.ofNullable(conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(member.getId()));
        LOGGER.info("[GetMemberConversation] Try get member(id: {})'s conversation. Status: {}.", member.getId(), conversationHistory.isPresent());
        return conversationHistory;
    }

    public void endConversation(final Member member) {
        checkNotNull(member);

        final long memberId = member.getId();
        LOGGER.info("[EndConversation] Try close member(id: {})'s active conversation history.", memberId);
        final ConversationHistory conversationHistory = conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(memberId);
        if (conversationHistory != null) {
            LOGGER.info("[EndConversation] Close member(id: {})'s conversation history(id: {}).");
            conversationHistoryDAO.updateEndTimeById(conversationHistory.getId(), toUnixTimestamp(DateTime.now()));
        } else {
            LOGGER.info("[EndConversation] Member(id: {}) don't has active conversation history.");
        }
    }

    public void updateConversationContent(final Member member, final String content) {
        checkNotNull(member);
        checkNotBlank(content);

        final long memberId = member.getId();
        LOGGER.info("[UpdateConversationContent] Try update member(id: {})'s active conversation history content to '{}'", memberId, content);
        final ConversationHistory conversationHistory = conversationHistoryDAO.getNotCompleteConversationHistoryByMemberId(memberId);
        if (conversationHistory == null) {
            LOGGER.warn("[UpdateConversationContent] Member(id: {}) don't have active conversation history.", memberId);
        } else {
            long conversationHistoryId = conversationHistory.getId();
            conversationHistoryDAO.updateContentById(conversationHistoryId, content);
            LOGGER.info("[UpdateConversationContent] Update member(id: {})'s conversation history(id: {})'s content from '{}' to '{}'.", memberId, conversationHistoryId, conversationHistory.getContent(), content);
        }
    }
}
