package com.thoughtworks.wechat_core.wechat.outbound;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WeChatEmptyMessageTest {
    @Test
    public void testToString() throws Exception {
        String result = new WeChatEmptyMessage().toString();
        assertThat(result, equalTo(""));
    }
}