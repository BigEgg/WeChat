package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessage;

public interface WorkflowStep {
    WorkflowStepResult handle(final InboundMessage inboundMessage, final WorkflowStepContext context);
}
