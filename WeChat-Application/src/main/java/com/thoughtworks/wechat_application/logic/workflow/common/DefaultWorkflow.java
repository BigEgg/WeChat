package com.thoughtworks.wechat_application.logic.workflow.common;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.logic.workflow.AbstractWorkflow;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowLevel;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowLevelAnnotation;
import com.thoughtworks.wechat_application.logic.workflow.common.steps.DefaultWorkflowStep;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;

import java.util.Arrays;

@Singleton
@WorkflowLevelAnnotation(level = WorkflowLevel.DEFAULT)
public class DefaultWorkflow extends AbstractWorkflow {
    @Inject
    public DefaultWorkflow(DefaultWorkflowStep defaultWorkflowStep) {
        super(Arrays.asList(defaultWorkflowStep));
    }

    @Override
    public boolean canStartHandle(InboundMessageEnvelop inboundMessageEnvelop) {
        return true;
    }
}
