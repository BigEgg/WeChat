package com.thoughtworks.wechat_application.logic.workflow.subscribe;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.logic.workflow.BasicWorkflowContext;
import com.thoughtworks.wechat_application.logic.workflow.Workflow;
import com.thoughtworks.wechat_application.logic.workflow.subscribe.steps.SubscribeWorkflowStep;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessage;
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
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SubscribeWorkflowTest {
    @Mock
    private SubscribeWorkflowStep subscribeWorkflowStep;
    private SubscribeWorkflow subscribeWorkflow;

    @Before
    public void setUp() throws Exception {
        subscribeWorkflow = new SubscribeWorkflow(subscribeWorkflowStep);
    }

    @Test
    public void testInject() throws Exception {
        final Injector injector = Guice.createInjector(binder -> {
            binder.bind(SubscribeWorkflowStep.class).toInstance(subscribeWorkflowStep);
        });

        final Workflow workflow = injector.getInstance(SubscribeWorkflow.class);
        assertThat(workflow, notNullValue());
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final Injector injector = Guice.createInjector(binder -> {
            binder.bind(SubscribeWorkflowStep.class).toInstance(subscribeWorkflowStep);
        });

        final Workflow workflow = injector.getInstance(SubscribeWorkflow.class);
        final Workflow anotherWorkflow = injector.getInstance(SubscribeWorkflow.class);
        assertThat(workflow, equalTo(anotherWorkflow));
    }

    @Test
    public void testCanHandle_SubscribeEvent() throws Exception {
        final boolean canHandle = subscribeWorkflow.canHandle(createSubscribeEventEnvelop(), new BasicWorkflowContext());

        assertThat(canHandle, equalTo(true));
    }

    @Test
    public void testCanHandle_Not_SubscribeEvent() throws Exception {
        final boolean canHandle = subscribeWorkflow.canHandle(new InboundMessageEnvelop("fromUser", "toUser", mock(InboundMessage.class)), new BasicWorkflowContext());

        assertThat(canHandle, equalTo(false));
    }

    private InboundMessageEnvelop createSubscribeEventEnvelop() {
        final WeChatSubscribeEvent event = new WeChatSubscribeEvent("toUser", "fromUser", 1422800623, "event", "subscribe");
        final InboundSubscribeEvent inboundSubscribeEvent = new InboundSubscribeEvent(event);

        return new InboundMessageEnvelop("fromUser", "toUser", inboundSubscribeEvent);
    }
}