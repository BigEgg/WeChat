package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_application.logic.workflow.exception.WorkflowNotSupportMessageException;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;

public interface Workflow {
    boolean canStartHandle(final InboundMessageEnvelop inboundMessageEnvelop);

    WorkflowResult handle(final InboundMessageEnvelop inboundMessageEnvelop, final WorkflowContext workflowContext) throws WorkflowNotSupportMessageException;
}
