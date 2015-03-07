package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class BasicWorkflowStepContext implements WorkflowStepContext {
    private Optional<OutboundMessage> outboundMessage;
    private Member member;

    public BasicWorkflowStepContext(final Member member) {
        this.outboundMessage = Optional.empty();
        this.member = member;
    }

    @Override
    public Optional<OutboundMessage> getOutboundMessage() {
        return outboundMessage;
    }

    public void setOutboundMessage(OutboundMessage outboundMessage) {
        checkNotNull(outboundMessage);

        this.outboundMessage = Optional.of(outboundMessage);
    }

    @Override
    public Member getMember() {
        return member;
    }
}
