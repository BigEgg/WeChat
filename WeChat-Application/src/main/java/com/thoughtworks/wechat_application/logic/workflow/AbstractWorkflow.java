package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_application.logic.workflow.exception.WorkflowNotSupportMessageException;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractWorkflow implements Workflow {
    protected static Logger LOGGER;
    private final List<WorkflowStep> steps;

    public AbstractWorkflow(final List<WorkflowStep> steps) {
        this.steps = steps;
    }

    @Override
    public Optional<OutboundMessage> handle(final InboundMessageEnvelop inboundMessageEnvelop, final WorkflowContext workflowContext) throws WorkflowNotSupportMessageException {
        checkNotNull(inboundMessageEnvelop);
        checkNotNull(workflowContext);
        if (!canHandle(inboundMessageEnvelop, workflowContext)) {
            throw new WorkflowNotSupportMessageException();
        }
        if (LOGGER == null) {
            LOGGER = LoggerFactory.getLogger(Workflow.class);
        }


        for (WorkflowStep step : steps) {
            WorkflowStepResult result = step.handle(inboundMessageEnvelop, workflowContext);
            switch (result) {
                case NEXT_STEP:
                    LOGGER.info("[Handle] Step '{}' ask go to next step.", step.getClass().toString());
                    continue;
                case WORKFLOW_COMPLETE:
                    LOGGER.info("[Handle] Step '{}' mark the workflow as complete.", step.getClass().toString());
                    return workflowContext.getOutboundMessage();
                case ABORT:
                    LOGGER.info("[Handle] Step '{}' said abort the workflow.", step.getClass().toString());
                    return Optional.empty();
            }
        }
        LOGGER.warn("[Handle] All step passed, none of it mark the workflow as complete.");
        return workflowContext.getOutboundMessage();
    }
}
