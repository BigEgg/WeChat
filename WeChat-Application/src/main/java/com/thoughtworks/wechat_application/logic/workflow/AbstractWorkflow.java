package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_application.logic.workflow.exception.WorkflowNeverCompleteException;
import com.thoughtworks.wechat_application.logic.workflow.exception.WorkflowNotSupportMessageException;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractWorkflow implements Workflow {
    protected static Logger LOGGER;
    private final List<WorkflowStep> steps;

    public AbstractWorkflow(final List<WorkflowStep> steps) {
        this.steps = steps;
    }

    @Override
    public WorkflowResult handle(final InboundMessageEnvelop inboundMessageEnvelop, final WorkflowContext workflowContext) throws WorkflowNotSupportMessageException {
        checkNotNull(inboundMessageEnvelop);
        checkNotNull(workflowContext);
        if (!canStartHandle(inboundMessageEnvelop)) {
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
                case STEP_COMPLETE:
                    LOGGER.info("[Handle] Step '{}' mark the step as complete.", step.getClass().toString());
                    return WorkflowResult.COMPLETE_NOT_FINISHED;
                case ABORT:
                    LOGGER.info("[Handle] Step '{}' said abort the workflow.", step.getClass().toString());
                    return WorkflowResult.ABORT;
                case WORKFLOW_COMPLETE:
                    LOGGER.info("[Handle] Step '{}' mark the step as complete.", step.getClass().toString());
                    return WorkflowResult.FINISHED;
            }
        }
        LOGGER.warn("[Handle] All step passed, none of it mark the workflow as complete.");
        throw new WorkflowNeverCompleteException();
    }
}
