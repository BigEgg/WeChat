package com.thoughtworks.wechat_application.inject;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.thoughtworks.wechat_application.logic.workflow.Workflow;
import com.thoughtworks.wechat_application.logic.workflow.common.DefaultWorkflow;
import com.thoughtworks.wechat_application.logic.workflow.subscribe.SubscribeWorkflow;

public class WorkflowModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Workflow> workflowBinder = Multibinder.newSetBinder(binder(), Workflow.class);
        workflowBinder.addBinding().to(SubscribeWorkflow.class);
        workflowBinder.addBinding().to(DefaultWorkflow.class);
    }
}
