package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_application.core.Member;

import java.util.Optional;

public interface WorkflowStepContext {
    Optional<OutboundMessage> getOutboundMessage();

    Member getMember();
}
