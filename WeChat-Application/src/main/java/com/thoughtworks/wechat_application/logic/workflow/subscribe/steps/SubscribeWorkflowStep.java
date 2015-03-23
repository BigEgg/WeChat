package com.thoughtworks.wechat_application.logic.workflow.subscribe.steps;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.jdbi.core.Member;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowContext;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowStep;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowStepResult;
import com.thoughtworks.wechat_application.logic.workflow.exception.WorkflowNotSupportMessageException;
import com.thoughtworks.wechat_application.services.EventLogService;
import com.thoughtworks.wechat_application.services.MemberService;
import com.thoughtworks.wechat_application.services.admin.AdminResourceKeys;
import com.thoughtworks.wechat_application.services.admin.AdminResourceService;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.inbound.event.InboundSubscribeEvent;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class SubscribeWorkflowStep implements WorkflowStep {
    private final static Logger LOGGER = LoggerFactory.getLogger(SubscribeWorkflowStep.class);
    private EventLogService eventLogService;
    private MemberService memberService;
    private AdminResourceService adminResourceService;

    @Inject
    public SubscribeWorkflowStep(final MemberService memberService,
                                 final EventLogService eventLogService,
                                 final AdminResourceService adminResourceService) {
        this.eventLogService = eventLogService;
        this.memberService = memberService;
        this.adminResourceService = adminResourceService;
    }

    @Override
    public WorkflowStepResult handle(InboundMessageEnvelop inboundMessageEnvelop, WorkflowContext context) {
        checkNotNull(inboundMessageEnvelop);
        checkNotNull(context);
        if (!(inboundMessageEnvelop.getMessage() instanceof InboundSubscribeEvent)) {
            throw new WorkflowNotSupportMessageException();
        }

        String memberOpenId = inboundMessageEnvelop.getFromUser();
        LOGGER.info("[Handle] Member(openid: {}) subscribe WeChat account.", memberOpenId);
        Member member = memberService.subscribeMember(memberOpenId);
        eventLogService.member().subscribe(member, DateTime.now());

        Optional<OutboundMessage> responseMessage = adminResourceService.getMessageResource(AdminResourceKeys.SUBSCRIBE_RESPONSE);
        context.setOutboundMessage(responseMessage);
        LOGGER.info("[Handle] Set subscribe response message. Have message? {}.", responseMessage.isPresent());

        return WorkflowStepResult.WORKFLOW_COMPLETE;
    }
}
