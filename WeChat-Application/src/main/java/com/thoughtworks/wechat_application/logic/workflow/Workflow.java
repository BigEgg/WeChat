package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessage;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_application.logic.workflow.exception.WorkflowNotSupportMessageException;

import java.util.Optional;

public interface Workflow {
    boolean canHandle(final InboundMessage inboundMessage);

    Optional<OutboundMessage> handle(final InboundMessage inboundMessage, final Member member) throws WorkflowNotSupportMessageException;
}
