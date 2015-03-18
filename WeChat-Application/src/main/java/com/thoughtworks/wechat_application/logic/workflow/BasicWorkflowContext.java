package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;

import java.util.Optional;

public class BasicWorkflowContext implements WorkflowContext {
    private Optional<OutboundMessage> outboundMessage;
    private Optional<String> conversationContent;
    private boolean saveConversationContent;

    public BasicWorkflowContext() {
        this.outboundMessage = Optional.empty();
        this.conversationContent = Optional.empty();
        this.saveConversationContent = false;
    }

    @Override
    public boolean getSaveConversationContent() {
        return saveConversationContent;
    }

    @Override
    public void setSaveConversationContent(boolean saveConversationContent) {
        this.saveConversationContent = saveConversationContent;
    }

    @Override
    public Optional<String> getConversationContent() {
        return conversationContent;
    }

    @Override
    public void setConversationContent(Optional<String> conversationContent) {
        this.conversationContent = conversationContent;
    }

    @Override
    public Optional<OutboundMessage> getOutboundMessage() {
        return outboundMessage;
    }

    @Override
    public void setOutboundMessage(Optional<OutboundMessage> outboundMessage) {
        this.outboundMessage = outboundMessage;
    }
}
