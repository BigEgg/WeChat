package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;

public interface WorkflowStep {
    WorkflowStepResult handle(final InboundMessageEnvelop inboundMessageEnvelop, final WorkflowContext context);
}
