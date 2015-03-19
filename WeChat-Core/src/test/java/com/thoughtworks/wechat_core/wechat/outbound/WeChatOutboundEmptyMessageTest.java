package com.thoughtworks.wechat_core.wechat.outbound;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WeChatOutboundEmptyMessageTest {
    @Test
    public void testToString() throws Exception {
        String result = new WeChatOutboundEmptyMessage().toString();
        assertThat(result, equalTo(""));
    }
}