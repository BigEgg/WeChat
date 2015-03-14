package com.thoughtworks.wechat_application.logic.workflow;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class BasicWorkflowContextTest {
    @Test
    public void testConstructor() throws Exception {
        final BasicWorkflowContext context = new BasicWorkflowContext();

        assertThat(context.getOutboundMessage().isPresent(), equalTo(false));
        assertThat(context.getConversationContent(), equalTo(""));
        assertThat(context.getSaveConversationContent(), equalTo(false));
    }
}