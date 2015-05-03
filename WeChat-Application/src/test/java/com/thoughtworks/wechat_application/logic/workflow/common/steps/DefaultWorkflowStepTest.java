package com.thoughtworks.wechat_application.logic.workflow.common.steps;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.logic.workflow.BasicWorkflowContext;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowStepResult;
import com.thoughtworks.wechat_application.models.systemMessage.TextSystemMessage;
import com.thoughtworks.wechat_application.services.admin.AdminResourceKey;
import com.thoughtworks.wechat_application.services.admin.AdminResourceService;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkflowStepTest {
    @Mock
    private AdminResourceService adminResourceService;
    private DefaultWorkflowStep step;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(binder -> {
            binder.bind(AdminResourceService.class).toInstance(adminResourceService);
        });

        step = injector.getInstance(DefaultWorkflowStep.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final DefaultWorkflowStep anotherDefaultWorkflowStep = injector.getInstance(DefaultWorkflowStep.class);
        assertThat(step, equalTo(anotherDefaultWorkflowStep));
    }

    @Test
    public void testHandle() throws Exception {
        when(adminResourceService.systemMessage()).thenReturn(mock(AdminResourceService.SystemMessageService.class));
        when(adminResourceService.systemMessage().getMessageResource(AdminResourceKey.DEFAULT_RESPONSE)).thenReturn(new TextSystemMessage("Content"));

        final BasicWorkflowContext context = new BasicWorkflowContext();
        final WorkflowStepResult result = step.handle(mock(InboundMessageEnvelop.class), context);

        verify(adminResourceService.systemMessage()).getMessageResource(eq(AdminResourceKey.DEFAULT_RESPONSE));
        assertThat(result, equalTo(WorkflowStepResult.WORKFLOW_COMPLETE));
        assertThat(context.getOutboundMessage().isPresent(), equalTo(true));
    }
}