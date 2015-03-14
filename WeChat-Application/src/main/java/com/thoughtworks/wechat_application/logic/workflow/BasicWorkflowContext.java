package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class BasicWorkflowContext implements WorkflowContext {
    private Optional<OutboundMessage> outboundMessage;
    private String conversationContent;
    private boolean saveConversationContent;

    public BasicWorkflowContext() {
        this.outboundMessage = Optional.empty();
        this.conversationContent = "";
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
    public String getConversationContent() {
        return conversationContent;
    }

    @Override
    public void setConversationContent(String conversationContent) {
        this.conversationContent = conversationContent;
    }

    @Override
    public Optional<OutboundMessage> getOutboundMessage() {
        return outboundMessage;
    }

    @Override
    public void setOutboundMessage(OutboundMessage outboundMessage) {
        checkNotNull(outboundMessage);

        this.outboundMessage = Optional.of(outboundMessage);
    }
}
