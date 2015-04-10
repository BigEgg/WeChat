package com.thoughtworks.wechat_application.logic.workflow.common.steps;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowContext;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowStep;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowStepResult;
import com.thoughtworks.wechat_application.services.admin.AdminResourceKey;
import com.thoughtworks.wechat_application.services.admin.AdminResourceService;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DefaultWorkflowStep implements WorkflowStep {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultWorkflowStep.class);
    private final AdminResourceService adminResourceService;

    @Inject
    public DefaultWorkflowStep(AdminResourceService adminResourceService) {
        this.adminResourceService = adminResourceService;
    }

    @Override
    public WorkflowStepResult handle(InboundMessageEnvelop inboundMessageEnvelop, WorkflowContext context) {
        context.setOutboundMessage(adminResourceService.getMessageResource(AdminResourceKey.DEFAULT_RESPONSE));
        return WorkflowStepResult.WORKFLOW_COMPLETE;
    }
}
