package com.thoughtworks.wechat_application.logic.workflow.subscribe;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.logic.workflow.AbstractWorkflow;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowLevel;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowLevelAnnotation;
import com.thoughtworks.wechat_application.logic.workflow.subscribe.steps.SubscribeWorkflowStep;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.inbound.event.InboundSubscribeEvent;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Singleton
@WorkflowLevelAnnotation(level = WorkflowLevel.SPECIFIC)
public class SubscribeWorkflow extends AbstractWorkflow {
    @Inject
    public SubscribeWorkflow(SubscribeWorkflowStep subscribeWorkflowStep) {
        super(Arrays.asList(subscribeWorkflowStep));
    }

    static {
        LOGGER = LoggerFactory.getLogger(SubscribeWorkflow.class);
    }

    @Override
    public boolean canStartHandle(InboundMessageEnvelop inboundMessageEnvelop) {
        return inboundMessageEnvelop.getMessage() instanceof InboundSubscribeEvent;
    }
}
