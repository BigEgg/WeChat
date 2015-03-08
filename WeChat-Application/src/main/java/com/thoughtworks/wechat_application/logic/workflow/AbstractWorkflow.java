package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_application.logic.workflow.exception.WorkflowNotSupportMessageException;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessage;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
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
    public Optional<OutboundMessage> handle(final InboundMessage inboundMessage, final Member member) throws WorkflowNotSupportMessageException {
        checkNotNull(inboundMessage);
        checkNotNull(member);

        final BasicWorkflowStepContext context = new BasicWorkflowStepContext(member);
        for (WorkflowStep step : steps) {
            WorkflowStepResult result = step.handle(inboundMessage, context);
            switch (result) {
                case NEXT_STEP:
                    LOGGER.info("[Handle] Step '{}' ask go to next step.", step.getClass().toString());
                    continue;
                case WORKFLOW_COMPLETE:
                    LOGGER.info("[Handle] Step '{}' mark the workflow as complete.", step.getClass().toString());
                    return context.getOutboundMessage();
                case ABORT:
                    LOGGER.info("[Handle] Step '{}' said abort the workflow.", step.getClass().toString());
                    return Optional.empty();
            }
        }
        LOGGER.warn("[Handle] All step passed, none of it mar the workflow as complete.");
        return context.getOutboundMessage();
    }
}
