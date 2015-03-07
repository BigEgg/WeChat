package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessage;
import com.thoughtworks.wechat_core.messages.inbound.event.SubscribeEvent;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_application.logic.workflow.exception.WorkflowNotSupportMessageException;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractWorkflow<T extends InboundMessage> implements Workflow {
    protected static Logger LOGGER;
    private final List<WorkflowStep> steps;

    public AbstractWorkflow(final List<WorkflowStep> steps) {
        this.steps = steps;
    }

    @Override
    public boolean canHandle(final InboundMessage inboundMessage) {
        checkNotNull(inboundMessage);

        T message = (T) inboundMessage;
        return message != null && canHandleCore(message);
    }

    @Override
    public Optional<OutboundMessage> handle(final InboundMessage inboundMessage, final Member member) throws WorkflowNotSupportMessageException {
        checkNotNull(inboundMessage);
        if (!(inboundMessage instanceof SubscribeEvent)) {
            throw new WorkflowNotSupportMessageException(
                    String.format("Only support SubscribeEvent, current message type: %s", inboundMessage.getClass().toString())
            );
        }

        final BasicWorkflowStepContext context = new BasicWorkflowStepContext(member);
        for (WorkflowStep step : steps) {
            WorkflowStepResult result = step.handle(inboundMessage, context);
            switch (result) {
                case NEXT_STEP:
                    continue;
                case WORKFLOW_COMPLETE:
                    return context.getOutboundMessage();
                case ABORT:
                    return Optional.empty();
            }
        }
        return context.getOutboundMessage();
    }

    protected boolean canHandleCore(T inboundMessage) {
        return true;
    }
}
