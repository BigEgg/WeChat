package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;

import java.util.Optional;

public interface WorkflowContext {
    boolean getSaveConversationContent();

    void setSaveConversationContent(final boolean saveConversationContent);

    String getConversationContent();

    void setConversationContent(final String conversationContent);

    Optional<OutboundMessage> getOutboundMessage();

    void setOutboundMessage(final Optional<OutboundMessage> outboundMessage);
}
