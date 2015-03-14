package com.thoughtworks.wechat_application.logic.workflow.subscribe.steps;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.logic.workflow.BasicWorkflowContext;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowStepResult;
import com.thoughtworks.wechat_application.services.EventLogService;
import com.thoughtworks.wechat_application.services.MemberService;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.inbound.event.InboundSubscribeEvent;
import com.thoughtworks.wechat_core.wechat.inbound.event.WeChatSubscribeEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SubscribeWorkflowStepTest {
    @Mock
    private EventLogService eventLogService;
    @Mock
    private MemberService memberService;
    private SubscribeWorkflowStep step;

    @Before
    public void setUp() throws Exception {
        step = new SubscribeWorkflowStep(memberService, eventLogService);
    }

    @Test
    public void testInject() throws Exception {
        final Injector injector = Guice.createInjector(binder -> {
            binder.bind(EventLogService.class).toInstance(eventLogService);
            binder.bind(MemberService.class).toInstance(memberService);
        });

        final SubscribeWorkflowStep subscribeWorkflowStep = injector.getInstance(SubscribeWorkflowStep.class);
        assertThat(subscribeWorkflowStep, notNullValue());
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final Injector injector = Guice.createInjector(binder -> {
            binder.bind(EventLogService.class).toInstance(eventLogService);
            binder.bind(MemberService.class).toInstance(memberService);
        });

        final SubscribeWorkflowStep subscribeWorkflowStep = injector.getInstance(SubscribeWorkflowStep.class);
        final SubscribeWorkflowStep anotherSubscribeWorkflowStep = injector.getInstance(SubscribeWorkflowStep.class);
        assertThat(subscribeWorkflowStep, equalTo(anotherSubscribeWorkflowStep));
    }

    @Test
    public void testHandle() throws Exception {
        final BasicWorkflowContext context = new BasicWorkflowContext();
        final WorkflowStepResult result = step.handle(createSubscribeEventEnvelop(), context);
    }

    private InboundMessageEnvelop createSubscribeEventEnvelop() {
        final WeChatSubscribeEvent event = new WeChatSubscribeEvent("toUser", "fromUser", 1422800623, "event", "subscribe");
        final InboundSubscribeEvent inboundSubscribeEvent = new InboundSubscribeEvent(event);

        return new InboundMessageEnvelop("fromUser", "toUser", inboundSubscribeEvent);
    }
}