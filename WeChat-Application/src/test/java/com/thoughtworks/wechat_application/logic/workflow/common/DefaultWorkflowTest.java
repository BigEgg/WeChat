package com.thoughtworks.wechat_application.logic.workflow.common;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowLevel;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowLevelAnnotation;
import com.thoughtworks.wechat_application.logic.workflow.common.steps.DefaultWorkflowStep;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkflowTest {
    @Mock
    private DefaultWorkflowStep step;
    private DefaultWorkflow workflow;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(binder -> {
            binder.bind(DefaultWorkflowStep.class).toInstance(step);
        });

        workflow = injector.getInstance(DefaultWorkflow.class);
    }

    @Test
    public void testWorkflowLevel() throws Exception {
        assertThat(workflow.getClass().isAnnotationPresent(WorkflowLevelAnnotation.class), equalTo(true));
        assertThat(workflow.getClass().getAnnotation(WorkflowLevelAnnotation.class).level(), equalTo(WorkflowLevel.DEFAULT));
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final DefaultWorkflow anotherDefaultWorkflow = injector.getInstance(DefaultWorkflow.class);
        assertThat(workflow, equalTo(anotherDefaultWorkflow));
    }

    @Test
    public void testCanStartHandle() throws Exception {
        boolean canStartHandle = workflow.canStartHandle(mock(InboundMessageEnvelop.class));

        assertThat(canStartHandle, equalTo(true));
    }
}