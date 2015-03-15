package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_application.logic.workflow.exception.WorkflowNotSupportMessageException;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.inbound.event.InboundSubscribeEvent;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_core.wechat.inbound.event.WeChatSubscribeEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AbstractWorkflowTest {
    @Mock
    private WorkflowStep workflowStep;
    private MockWorkflow workflow;

    @Before
    public void setUp() throws Exception {
        workflow = new MockWorkflow(workflowStep);
    }

    @Test(expected = WorkflowNotSupportMessageException.class)
    public void testHandle_CannotHandle() throws Exception {
        final InboundMessageEnvelop subscribeEventEnvelop = createSubscribeEventEnvelop();
        final WorkflowContext workflowContent = createWorkflowContent();

        when(workflowStep.handle(subscribeEventEnvelop, workflowContent)).thenReturn(WorkflowStepResult.NEXT_STEP);
        MockWorkflow mockWorkflow = new MockWorkflow(workflowStep);
        mockWorkflow.setCanHandle(false);
        mockWorkflow.handle(subscribeEventEnvelop, workflowContent);
    }

    @Test
    public void testHandle_NextStep() throws Exception {
        final InboundMessageEnvelop subscribeEventEnvelop = createSubscribeEventEnvelop();
        final WorkflowContext workflowContent = createWorkflowContent();

        when(workflowStep.handle(subscribeEventEnvelop, workflowContent)).thenReturn(WorkflowStepResult.NEXT_STEP);

        final Optional<OutboundMessage> message = workflow.handle(subscribeEventEnvelop, workflowContent);

        verify(workflowStep, times(1)).handle(eq(subscribeEventEnvelop), eq(workflowContent));
        assertThat(message.isPresent(), equalTo(false));
    }

    @Test
    public void testHandle_Abort() throws Exception {
        final InboundMessageEnvelop subscribeEventEnvelop = createSubscribeEventEnvelop();
        final WorkflowContext workflowContent = createWorkflowContent();

        when(workflowStep.handle(subscribeEventEnvelop, workflowContent)).thenReturn(WorkflowStepResult.ABORT);

        final Optional<OutboundMessage> message = workflow.handle(subscribeEventEnvelop, workflowContent);

        verify(workflowStep, times(1)).handle(eq(subscribeEventEnvelop), eq(workflowContent));
        assertThat(message.isPresent(), equalTo(false));
    }

    @Test
    public void testHandle_WorkflowComplete() throws Exception {
        final InboundMessageEnvelop subscribeEventEnvelop = createSubscribeEventEnvelop();
        final WorkflowContext workflowContent = createWorkflowContent();

        when(workflowStep.handle(subscribeEventEnvelop, workflowContent)).thenAnswer(answer -> {
            final BasicWorkflowContext context = answer.getArgumentAt(1, BasicWorkflowContext.class);
            context.setOutboundMessage(Optional.of(mock(OutboundMessage.class)));
            return WorkflowStepResult.WORKFLOW_COMPLETE;
        });

        final Optional<OutboundMessage> message = workflow.handle(subscribeEventEnvelop, workflowContent);

        assertThat(message.isPresent(), equalTo(true));
    }

    private InboundMessageEnvelop createSubscribeEventEnvelop() {
        final WeChatSubscribeEvent event = new WeChatSubscribeEvent("toUser", "fromUser", 1422800623, "event", "subscribe");
        final InboundSubscribeEvent inboundSubscribeEvent = new InboundSubscribeEvent(event);

        return new InboundMessageEnvelop("fromUser", "toUser", inboundSubscribeEvent);
    }

    private Member createSubscribeMember() {
        return new Member(1L, "openId", true);
    }

    private WorkflowContext createWorkflowContent() {
        return new BasicWorkflowContext();
    }

    public class MockWorkflow extends AbstractWorkflow {
        private boolean canHandle = true;

        {
            LOGGER = LoggerFactory.getLogger(MockWorkflow.class);
        }

        public MockWorkflow(WorkflowStep step) {
            super(Arrays.asList(step));
        }

        public void setCanHandle(boolean canHandle) {
            this.canHandle = canHandle;
        }

        @Override
        public boolean canHandle(InboundMessageEnvelop inboundMessageEnvelop, WorkflowContext workflowContext) {
            return this.canHandle;
        }
    }
}