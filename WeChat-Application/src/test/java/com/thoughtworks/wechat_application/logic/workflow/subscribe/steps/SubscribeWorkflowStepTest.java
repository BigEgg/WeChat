package com.thoughtworks.wechat_application.logic.workflow.subscribe.steps;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_application.logic.workflow.BasicWorkflowContext;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowStepResult;
import com.thoughtworks.wechat_application.logic.workflow.exception.WorkflowNotSupportMessageException;
import com.thoughtworks.wechat_application.services.EventLogService;
import com.thoughtworks.wechat_application.services.MemberService;
import com.thoughtworks.wechat_application.services.admin.AdminResourceKeys;
import com.thoughtworks.wechat_application.services.admin.AdminResourceService;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessage;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.inbound.event.InboundSubscribeEvent;
import com.thoughtworks.wechat_core.messages.outbound.messages.OutboundTextMessage;
import com.thoughtworks.wechat_core.wechat.inbound.event.WeChatSubscribeEvent;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubscribeWorkflowStepTest {
    @Mock
    private EventLogService eventLogService;
    @Mock
    private MemberService memberService;
    @Mock
    private AdminResourceService adminResourceService;
    private SubscribeWorkflowStep step;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(binder -> {
            binder.bind(EventLogService.class).toInstance(eventLogService);
            binder.bind(MemberService.class).toInstance(memberService);
            binder.bind(AdminResourceService.class).toInstance(adminResourceService);
        });

        step = injector.getInstance(SubscribeWorkflowStep.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final SubscribeWorkflowStep anotherSubscribeWorkflowStep = injector.getInstance(SubscribeWorkflowStep.class);
        assertThat(step, equalTo(anotherSubscribeWorkflowStep));
    }

    @Test
    public void testHandle() throws Exception {
        when(eventLogService.member()).thenReturn(mock(EventLogService.MemberEventLogService.class));
        when(adminResourceService.getMessageResource(AdminResourceKeys.SUBSCRIBE_RESPONSE)).thenReturn(Optional.of(new OutboundTextMessage("Content")));

        final BasicWorkflowContext context = new BasicWorkflowContext();
        final WorkflowStepResult result = step.handle(createSubscribeEventEnvelop(), context);

        verify(memberService).subscribeMember("fromUser");
        verify(eventLogService).member();
        verify(eventLogService.member()).subscribe(any(Member.class), any(DateTime.class));
        verify(adminResourceService).getMessageResource(eq(AdminResourceKeys.SUBSCRIBE_RESPONSE));
        assertThat(result, equalTo(WorkflowStepResult.WORKFLOW_COMPLETE));
        assertThat(context.getConversationContent().isPresent(), equalTo(false));
        assertThat(context.getSaveConversationContent(), equalTo(false));
        assertThat(context.getOutboundMessage().isPresent(), equalTo(true));
        assertThat(context.getOutboundMessage().get().getClass(), equalTo(OutboundTextMessage.class));
    }

    @Test(expected = WorkflowNotSupportMessageException.class)
    public void testHandle_NotSupportMessage() throws Exception {
        step.handle(new InboundMessageEnvelop("fromUser", "toUser", mock(InboundMessage.class)), new BasicWorkflowContext());
    }

    private InboundMessageEnvelop createSubscribeEventEnvelop() {
        final WeChatSubscribeEvent event = new WeChatSubscribeEvent("toUser", "fromUser", 1422800623, "event", "subscribe");
        final InboundSubscribeEvent inboundSubscribeEvent = new InboundSubscribeEvent(event);

        return new InboundMessageEnvelop("fromUser", "toUser", inboundSubscribeEvent);
    }
}