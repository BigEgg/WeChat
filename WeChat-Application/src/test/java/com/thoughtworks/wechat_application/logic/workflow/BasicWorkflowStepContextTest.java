package com.thoughtworks.wechat_application.logic.workflow;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_application.core.Member;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class BasicWorkflowStepContextTest {
    @Test
    public void testConstructor() throws Exception {
        final Member member = createUnsubscribeMember();
        final BasicWorkflowStepContext context = new BasicWorkflowStepContext(member);

        assertThat(context.getMember(), equalTo(member));
        assertThat(context.getOutboundMessage().isPresent(), equalTo(false));
    }

    @Test
    public void testSetOutboundMessage() throws Exception {
        final Member member = createUnsubscribeMember();
        final BasicWorkflowStepContext context = new BasicWorkflowStepContext(member);

        context.setOutboundMessage(mock(OutboundMessage.class));

        assertThat(context.getOutboundMessage().isPresent(), equalTo(true));
    }

    private Member createUnsubscribeMember() {
        return new Member(1L, "openId", false);
    }
}