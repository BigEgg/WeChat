package com.thoughtworks.wechat_application.logic.workflow.subscribe;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.logic.workflow.AbstractWorkflow;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowContext;
import com.thoughtworks.wechat_application.logic.workflow.subscribe.steps.SubscribeWorkflowStep;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.inbound.event.InboundSubscribeEvent;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Singleton
public class SubscribeWorkflow extends AbstractWorkflow {
    @Inject
    public SubscribeWorkflow(SubscribeWorkflowStep subscribeWorkflowStep) {
        super(Arrays.asList(subscribeWorkflowStep));
    }

    static {
        LOGGER = LoggerFactory.getLogger(SubscribeWorkflow.class);
    }

    @Override
    public boolean canHandle(InboundMessageEnvelop inboundMessageEnvelop, WorkflowContext workflowContext) {
        return inboundMessageEnvelop.getMessage() instanceof InboundSubscribeEvent;
    }
}
