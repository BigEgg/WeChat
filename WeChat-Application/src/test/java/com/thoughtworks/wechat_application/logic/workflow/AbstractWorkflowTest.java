package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_application.core.Member;
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

    @Test
    public void testHandle_NextStep() throws Exception {
        InboundSubscribeEvent subscribeEvent = createSubscribeEvent();
        Member subscribeMember = createSubscribeMember();

        when(workflowStep.handle(eq(subscribeEvent), any(WorkflowStepContext.class))).thenReturn(WorkflowStepResult.NEXT_STEP);

        Optional<OutboundMessage> message = workflow.handle(subscribeEvent, subscribeMember);

        verify(workflowStep, times(1)).handle(eq(subscribeEvent), any(WorkflowStepContext.class));
        assertThat(message.isPresent(), equalTo(false));
    }

    @Test
    public void testHandle_Abort() throws Exception {
        InboundSubscribeEvent subscribeEvent = createSubscribeEvent();
        Member subscribeMember = createSubscribeMember();

        when(workflowStep.handle(eq(subscribeEvent), any(WorkflowStepContext.class))).thenReturn(WorkflowStepResult.ABORT);

        Optional<OutboundMessage> message = workflow.handle(subscribeEvent, subscribeMember);

        verify(workflowStep, times(1)).handle(eq(subscribeEvent), any(WorkflowStepContext.class));
        assertThat(message.isPresent(), equalTo(false));
    }

    @Test
    public void testHandle_WorkflowComplete() throws Exception {
        InboundSubscribeEvent subscribeEvent = createSubscribeEvent();
        Member subscribeMember = createSubscribeMember();

        when(workflowStep.handle(eq(subscribeEvent), any(WorkflowStepContext.class))).thenAnswer(answer -> {
            BasicWorkflowStepContext context = answer.getArgumentAt(1, BasicWorkflowStepContext.class);
            context.setOutboundMessage(mock(OutboundMessage.class));
            return WorkflowStepResult.WORKFLOW_COMPLETE;
        });

        Optional<OutboundMessage> message = workflow.handle(subscribeEvent, subscribeMember);

        assertThat(message.isPresent(), equalTo(true));
    }

    private InboundSubscribeEvent createSubscribeEvent() {
        WeChatSubscribeEvent event = new WeChatSubscribeEvent("toUser", "fromUser", 1422800623, "event", "subscribe");
        return new InboundSubscribeEvent(event);
    }

    private Member createSubscribeMember() {
        return new Member(1L, "openId", true);
    }

    public class MockWorkflow extends AbstractWorkflow<InboundSubscribeEvent> {
        {
            LOGGER = LoggerFactory.getLogger(MockWorkflow.class);
        }

        public MockWorkflow(WorkflowStep step) {
            super(Arrays.asList(step));
        }
    }
}