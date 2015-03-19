package com.thoughtworks.wechat_application.logic.workflow;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import com.thoughtworks.wechat_application.core.ConversationHistory;
import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_application.logic.workflow.common.DefaultWorkflow;
import com.thoughtworks.wechat_application.logic.workflow.common.steps.DefaultWorkflowStep;
import com.thoughtworks.wechat_application.logic.workflow.subscribe.SubscribeWorkflow;
import com.thoughtworks.wechat_application.logic.workflow.subscribe.steps.SubscribeWorkflowStep;
import com.thoughtworks.wechat_application.services.ConversationHistoryService;
import com.thoughtworks.wechat_application.services.MemberService;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.inbound.event.InboundSubscribeEvent;
import com.thoughtworks.wechat_core.messages.inbound.messages.InboundTextMessage;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_core.wechat.inbound.event.WeChatSubscribeEvent;
import com.thoughtworks.wechat_core.wechat.inbound.message.WeChatInboundTextMessage;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowEngineTest {
    @Mock
    private SubscribeWorkflowStep subscribeWorkflowStep;
    @Mock
    private DefaultWorkflowStep defaultWorkflowStep;
    @Mock
    private Workflow mockWorkflow;
    @Mock
    private ConversationHistoryService conversationHistoryService;
    @Mock
    private MemberService memberService;
    private SubscribeWorkflow subscribeWorkflow;
    private DefaultWorkflow defaultWorkflow;
    private WorkflowEngine engine;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        when(subscribeWorkflowStep.handle(any(InboundMessageEnvelop.class), any(WorkflowContext.class))).thenAnswer(answer -> {
            final BasicWorkflowContext context = answer.getArgumentAt(1, BasicWorkflowContext.class);
            context.setOutboundMessage(Optional.of(mock(OutboundMessage.class)));
            return WorkflowStepResult.WORKFLOW_COMPLETE;
        });
        when(defaultWorkflowStep.handle(any(InboundMessageEnvelop.class), any(WorkflowContext.class))).thenAnswer(answer -> {
            final BasicWorkflowContext context = answer.getArgumentAt(1, BasicWorkflowContext.class);
            context.setOutboundMessage(Optional.of(mock(OutboundMessage.class)));
            return WorkflowStepResult.WORKFLOW_COMPLETE;
        });

        injector = Guice.createInjector(binder -> {
            binder.bind(ConversationHistoryService.class).toInstance(conversationHistoryService);
            binder.bind(MemberService.class).toInstance(memberService);
            binder.bind(SubscribeWorkflowStep.class).toInstance(subscribeWorkflowStep);
            binder.bind(DefaultWorkflowStep.class).toInstance(defaultWorkflowStep);
        }, new AbstractModule() {
            @Override
            protected void configure() {
                Multibinder<Workflow> workflowBinder = Multibinder.newSetBinder(binder(), Workflow.class);
                workflowBinder.addBinding().to(SubscribeWorkflow.class);
                workflowBinder.addBinding().to(DefaultWorkflow.class);
                workflowBinder.addBinding().toInstance(mockWorkflow);
            }
        });

        engine = injector.getInstance(WorkflowEngine.class);
        subscribeWorkflow = injector.getInstance(SubscribeWorkflow.class);
        defaultWorkflow = injector.getInstance(DefaultWorkflow.class);
    }

    @Test
    public void testInject_Singleton() throws Exception {
        final WorkflowEngine workflowEngine = injector.getInstance(WorkflowEngine.class);
        assertThat(workflowEngine, equalTo(engine));
    }

    @Test
    public void testHandle_MemberNotExist() {
        final InboundMessageEnvelop subscribeEventEnvelop = createSubscribeEventEnvelop();
        final Member subscribeMember = createSubscribedMember();

        when(memberService.getMemberByOpenId("fromUser")).thenReturn(Optional.<Member>empty(), Optional.of(subscribeMember));
        when(conversationHistoryService.startNewConversation(subscribeMember, "Subscribe")).thenReturn(createSubscribeConversationHistory());

        final Optional<OutboundMessage> outboundMessageOpt = engine.handle(subscribeEventEnvelop);

        verify(mockWorkflow, never()).canStartHandle(any(InboundMessageEnvelop.class));
        verify(memberService, times(2)).getMemberByOpenId(eq("fromUser"));
        verify(conversationHistoryService).startNewConversation(eq(subscribeMember), eq("Subscribe"));
        verify(conversationHistoryService).endConversation(eq(subscribeMember));
        assertThat(outboundMessageOpt.isPresent(), equalTo(true));
    }

    @Test
    public void testHandle_ExistMember_NewConversation_WorkflowFinished() {
        final InboundMessageEnvelop textEventEnvelop = createTextEventEnvelop();
        final Member subscribeMember = createSubscribedMember();

        when(memberService.getMemberByOpenId("fromUser")).thenReturn(Optional.of(subscribeMember));
        when(conversationHistoryService.getMemberConversation(subscribeMember)).thenReturn(Optional.<ConversationHistory>empty());
        when(mockWorkflow.canStartHandle(textEventEnvelop)).thenReturn(true);
        when(conversationHistoryService.startNewConversation(eq(subscribeMember), anyString())).thenReturn(createConversationHistory(getWorkflowName(mockWorkflow.getClass()), ""));
        when(mockWorkflow.handle(eq(textEventEnvelop), any(WorkflowContext.class))).thenAnswer(answer -> {
            final BasicWorkflowContext context = answer.getArgumentAt(1, BasicWorkflowContext.class);
            context.setOutboundMessage(Optional.of(mock(OutboundMessage.class)));
            return WorkflowResult.FINISHED;
        });

        final Optional<OutboundMessage> outboundMessageOpt = engine.handle(textEventEnvelop);

        verify(mockWorkflow).canStartHandle(any(InboundMessageEnvelop.class));
        verify(memberService).getMemberByOpenId(eq("fromUser"));
        verify(conversationHistoryService).getMemberConversation(eq(subscribeMember));
        verify(conversationHistoryService).startNewConversation(eq(subscribeMember), anyString());
        verify(conversationHistoryService, never()).updateConversationContent(any(Member.class), anyString());
        verify(conversationHistoryService).endConversation(eq(subscribeMember));
        assertThat(outboundMessageOpt.isPresent(), equalTo(true));
    }

    @Test
    public void testHandle_ExistMember_NewConversation_WorkflowNotFinished() {
        final InboundMessageEnvelop textEventEnvelop = createTextEventEnvelop();
        final Member subscribeMember = createSubscribedMember();

        when(memberService.getMemberByOpenId("fromUser")).thenReturn(Optional.of(subscribeMember));
        when(conversationHistoryService.getMemberConversation(subscribeMember)).thenReturn(Optional.<ConversationHistory>empty());
        when(mockWorkflow.canStartHandle(textEventEnvelop)).thenReturn(true);
        when(conversationHistoryService.startNewConversation(eq(subscribeMember), anyString())).thenReturn(createConversationHistory(getWorkflowName(mockWorkflow.getClass()), ""));
        when(mockWorkflow.handle(eq(textEventEnvelop), any(WorkflowContext.class))).thenAnswer(answer -> {
            final BasicWorkflowContext context = answer.getArgumentAt(1, BasicWorkflowContext.class);
            context.setOutboundMessage(Optional.of(mock(OutboundMessage.class)));
            context.setSaveConversationContent(true);
            context.setConversationContent(Optional.of("NewContent"));
            return WorkflowResult.COMPLETE_NOT_FINISHED;
        });

        final Optional<OutboundMessage> outboundMessageOpt = engine.handle(textEventEnvelop);

        verify(mockWorkflow).canStartHandle(any(InboundMessageEnvelop.class));
        verify(memberService).getMemberByOpenId(eq("fromUser"));
        verify(conversationHistoryService).getMemberConversation(eq(subscribeMember));
        verify(conversationHistoryService).startNewConversation(eq(subscribeMember), anyString());
        verify(conversationHistoryService).updateConversationContent(eq(subscribeMember), eq("NewContent"));
        verify(conversationHistoryService, never()).endConversation(any(Member.class));
        assertThat(outboundMessageOpt.isPresent(), equalTo(true));
    }

    @Test
    public void testHandle_ExistMember_ExistConversation_WorkflowFinished() {
        final InboundMessageEnvelop textEventEnvelop = createTextEventEnvelop();
        final Member subscribeMember = createSubscribedMember();

        when(memberService.getMemberByOpenId("fromUser")).thenReturn(Optional.of(subscribeMember));
        when(conversationHistoryService.getMemberConversation(subscribeMember)).thenReturn(Optional.of(createConversationHistory(getWorkflowName(mockWorkflow.getClass()), "Content")));
        when(mockWorkflow.canStartHandle(textEventEnvelop)).thenReturn(true);
        when(mockWorkflow.handle(eq(textEventEnvelop), any(WorkflowContext.class))).thenAnswer(answer -> {
            final BasicWorkflowContext context = answer.getArgumentAt(1, BasicWorkflowContext.class);
            context.setOutboundMessage(Optional.of(mock(OutboundMessage.class)));
            return WorkflowResult.FINISHED;
        });

        final Optional<OutboundMessage> outboundMessageOpt = engine.handle(textEventEnvelop);

        verify(mockWorkflow, never()).canStartHandle(any(InboundMessageEnvelop.class));
        verify(memberService).getMemberByOpenId(eq("fromUser"));
        verify(conversationHistoryService).getMemberConversation(eq(subscribeMember));
        verify(conversationHistoryService, never()).startNewConversation(eq(subscribeMember), anyString());
        verify(conversationHistoryService, never()).updateConversationContent(any(Member.class), anyString());
        verify(conversationHistoryService).endConversation(eq(subscribeMember));
        assertThat(outboundMessageOpt.isPresent(), equalTo(true));
    }

    @Test
    public void testHandle_ExistMember_ExistConversation_WorkflowNotFinished() {
        final InboundMessageEnvelop textEventEnvelop = createTextEventEnvelop();
        final Member subscribeMember = createSubscribedMember();

        when(memberService.getMemberByOpenId("fromUser")).thenReturn(Optional.of(subscribeMember));
        when(conversationHistoryService.getMemberConversation(subscribeMember)).thenReturn(Optional.of(createConversationHistory(getWorkflowName(mockWorkflow.getClass()), "Content")));
        when(mockWorkflow.canStartHandle(textEventEnvelop)).thenReturn(true);
        when(mockWorkflow.handle(eq(textEventEnvelop), any(WorkflowContext.class))).thenAnswer(answer -> {
            final BasicWorkflowContext context = answer.getArgumentAt(1, BasicWorkflowContext.class);
            context.setOutboundMessage(Optional.of(mock(OutboundMessage.class)));
            context.setSaveConversationContent(true);
            context.setConversationContent(Optional.of("NewContent"));
            return WorkflowResult.COMPLETE_NOT_FINISHED;
        });

        final Optional<OutboundMessage> outboundMessageOpt = engine.handle(textEventEnvelop);

        verify(mockWorkflow, never()).canStartHandle(any(InboundMessageEnvelop.class));
        verify(memberService).getMemberByOpenId(eq("fromUser"));
        verify(conversationHistoryService).getMemberConversation(eq(subscribeMember));
        verify(conversationHistoryService, never()).startNewConversation(eq(subscribeMember), anyString());
        verify(conversationHistoryService).updateConversationContent(eq(subscribeMember), eq("NewContent"));
        verify(conversationHistoryService, never()).endConversation(any(Member.class));
        assertThat(outboundMessageOpt.isPresent(), equalTo(true));
    }

    private InboundMessageEnvelop createSubscribeEventEnvelop() {
        final WeChatSubscribeEvent event = new WeChatSubscribeEvent("toUser", "fromUser", 1422800623, "event", "subscribe");
        final InboundSubscribeEvent inboundSubscribeEvent = new InboundSubscribeEvent(event);

        return new InboundMessageEnvelop("fromUser", "toUser", inboundSubscribeEvent);
    }

    private InboundMessageEnvelop createTextEventEnvelop() {
        final WeChatInboundTextMessage weChatInboundTextMessage = new WeChatInboundTextMessage("toUser", "fromUser", 1422800623, "text", "content", 1L);
        final InboundTextMessage inboundTextMessage = new InboundTextMessage(weChatInboundTextMessage);

        return new InboundMessageEnvelop("fromUser", "toUser", inboundTextMessage);
    }

    private Member createSubscribedMember() {
        return new Member(1L, "openId", true);
    }

    private ConversationHistory createSubscribeConversationHistory() {
        return new ConversationHistory(1L, 1L, "Subscribe", DateTime.now(), Optional.<DateTime>empty(), Optional.<String>empty());
    }

    private ConversationHistory createConversationHistory(final String workflowName, final String content) {
        return new ConversationHistory(1L, 1L, workflowName, DateTime.now(), Optional.<DateTime>empty(), Optional.of(content));
    }

    private <T extends Workflow> String getWorkflowName(final Class<T> clazz) {
        return clazz.getSimpleName().replace("Workflow", "");
    }
}