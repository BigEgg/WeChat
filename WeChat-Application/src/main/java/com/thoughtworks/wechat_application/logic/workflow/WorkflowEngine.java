package com.thoughtworks.wechat_application.logic.workflow;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.core.ConversationHistory;
import com.thoughtworks.wechat_application.core.Member;
import com.thoughtworks.wechat_application.logic.workflow.exception.NoSuchWorkflowException;
import com.thoughtworks.wechat_application.services.ConversationHistoryService;
import com.thoughtworks.wechat_application.services.MemberService;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class WorkflowEngine {
    private final static Logger LOGGER = LoggerFactory.getLogger(WorkflowEngine.class);
    private final Map<WorkflowLevel, Map<String, Workflow>> workflows;
    private final ConversationHistoryService conversationHistoryService;
    private final MemberService memberService;

    @Inject
    public WorkflowEngine(final Set<Workflow> workflowList,
                          final ConversationHistoryService conversationHistoryService,
                          final MemberService memberService) {
        this.conversationHistoryService = conversationHistoryService;
        this.memberService = memberService;

        this.workflows = Arrays.asList(WorkflowLevel.values()).stream().collect(Collectors.toMap((WorkflowLevel level) -> level, level -> new HashMap<>()));
        initializeWorkflows(workflowList);
    }

    public Optional<OutboundMessage> handle(InboundMessageEnvelop inboundMessageEnvelop) {
        checkNotNull(inboundMessageEnvelop);

        final String memberOpenId = inboundMessageEnvelop.getFromUser();
        final BasicWorkflowContext context = new BasicWorkflowContext();

        final Optional<Member> memberOpt = memberService.getMemberByOpenId(memberOpenId);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            LOGGER.info("[Handle] Exist member(id: {}) come with message(type: {}).", member.getId(), inboundMessageEnvelop.getMessage().getMessageType());
            handleExistMember(inboundMessageEnvelop, context, member);
        } else {
            LOGGER.info("[Handle] New User(openId: {}) come with message(type: {}).", inboundMessageEnvelop.getFromUser(), inboundMessageEnvelop.getMessage().getMessageType());
            handleNewMember(inboundMessageEnvelop, context);
        }

        Optional<OutboundMessage> outboundMessage = context.getOutboundMessage();
        LOGGER.info("[Handle] Get response message for member(openId: {}), status: {}.", memberOpenId, outboundMessage.isPresent());
        return outboundMessage;
    }

    public Map<WorkflowLevel, Integer> getWorkflowSize() {
        return workflows.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
    }

    private void handleExistMember(final InboundMessageEnvelop inboundMessageEnvelop, BasicWorkflowContext context, Member member) {
        Workflow workflow;
        ConversationHistory conversationHistory;

        final Optional<ConversationHistory> memberConversationOpt = conversationHistoryService.getMemberConversation(member);
        if (memberConversationOpt.isPresent()) {
            conversationHistory = memberConversationOpt.get();
            LOGGER.info("[HandleExistMember] Member(id: {}) already have one active conversation(id: {}).", member.getId(), conversationHistory.getId());
            context.setConversationContent(conversationHistory.getContent());
            workflow = getWorkflowByName(conversationHistory.getWorkflowName());
        } else {
            workflow = getCanStartHandleWorkflow(inboundMessageEnvelop);
            conversationHistory = conversationHistoryService.startNewConversation(member, getWorkflowName(workflow.getClass()));
            LOGGER.info("[HandleExistMember] Member(id: {}) don't have any active conversation, start a new one(id: {}).", member.getId(), conversationHistory.getId());
        }

        LOGGER.info("[HandleExistMember] Get workflow(name: {}) to handle this message.", getWorkflowName(workflow.getClass()));
        final WorkflowResult workflowResult = workflow.handle(inboundMessageEnvelop, context);
        if (context.getSaveConversationContent()) {
            final String newContent = context.getConversationContent().orElse("");
            conversationHistoryService.updateConversationContent(member, newContent);
            LOGGER.info("[HandleExistMember] Workflow need update the conversation(id: {}) content to '{}'.", conversationHistory.getId(), newContent);
        }

        if (workflowResult == WorkflowResult.FINISHED) {
            conversationHistoryService.endConversation(member);
            LOGGER.info("[HandleExistMember] Workflow complete member(id: {})'s conversation(id: {}).", member.getId(), conversationHistory.getId());
        }
    }

    private void handleNewMember(final InboundMessageEnvelop inboundMessageEnvelop, final BasicWorkflowContext context) {
        final Workflow workflow = getCanStartHandleWorkflow(inboundMessageEnvelop);
        LOGGER.info("[HandleNewMember] Get workflow(name: {}) to handle this message.", getWorkflowName(workflow.getClass()));
        workflow.handle(inboundMessageEnvelop, context);

        final Member member = memberService.getMemberByOpenId(inboundMessageEnvelop.getFromUser()).get();
        ConversationHistory conversationHistory = conversationHistoryService.startNewConversation(member, getWorkflowName(workflow.getClass()));
        LOGGER.info("[HandleNewMember] Record the conversation(id: {}) for member(id: {}).", conversationHistory.getId(), member.getId());
        conversationHistoryService.endConversation(member);
    }


    private void initializeWorkflows(final Set<Workflow> workflowList) {
        workflowList.stream().forEach(workflow -> {
            final WorkflowLevel level = workflow.getClass().isAnnotationPresent(WorkflowLevelAnnotation.class)
                    ? workflow.getClass().getAnnotation(WorkflowLevelAnnotation.class).level()
                    : WorkflowLevel.GENGERATE;

            workflows.get(level).put(getWorkflowName(workflow.getClass()), workflow);
        });
        workflows.entrySet().stream().forEach(entry -> {
            LOGGER.info("[InitializeWorkflows] Application have {} workflow in {} level.", entry.getKey().toString(), entry.getValue().size());
        });
    }

    private Workflow getWorkflowByName(final String workflowName) {
        for (Map<String, Workflow> stringWorkflowMap : workflows.values()) {
            for (Workflow workflow : stringWorkflowMap.values()) {
                if (getWorkflowName(workflow.getClass()).equalsIgnoreCase(workflowName)) {
                    return workflow;
                }
            }
        }
        throw new NoSuchWorkflowException();
    }

    private Workflow getCanStartHandleWorkflow(final InboundMessageEnvelop inboundMessageEnvelop) {
        Optional<Workflow> workflowOpt = getWorkflowByLevel(inboundMessageEnvelop, WorkflowLevel.SPECIFIC);
        if (workflowOpt.isPresent()) {
            return workflowOpt.get();
        }

        workflowOpt = getWorkflowByLevel(inboundMessageEnvelop, WorkflowLevel.GENGERATE);
        if (workflowOpt.isPresent()) {
            return workflowOpt.get();
        }

        workflowOpt = getWorkflowByLevel(inboundMessageEnvelop, WorkflowLevel.DEFAULT);

        return workflowOpt.orElseThrow(NoSuchWorkflowException::new);
    }

    private Optional<Workflow> getWorkflowByLevel(final InboundMessageEnvelop inboundMessageEnvelop, final WorkflowLevel level) {
        return workflows.get(level).values().stream().filter(wf -> wf.canStartHandle(inboundMessageEnvelop)).findFirst();
    }

    private <T extends Workflow> String getWorkflowName(final Class<T> clazz) {
        return clazz.getSimpleName().replace("Workflow", "");
    }
}
