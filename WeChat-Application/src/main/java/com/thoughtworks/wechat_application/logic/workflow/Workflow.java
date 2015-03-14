package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_application.logic.workflow.exception.WorkflowNotSupportMessageException;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;

import java.util.Optional;

public interface Workflow {
    boolean canHandle(final InboundMessageEnvelop inboundMessageEnvelop, final WorkflowContext workflowContext);

    Optional<OutboundMessage> handle(final InboundMessageEnvelop inboundMessageEnvelop, final WorkflowContext workflowContext) throws WorkflowNotSupportMessageException;
}
