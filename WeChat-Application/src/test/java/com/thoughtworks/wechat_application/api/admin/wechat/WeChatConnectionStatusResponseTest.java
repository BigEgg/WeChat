package com.thoughtworks.wechat_application.api.admin.wechat;

import com.thoughtworks.wechat_application.api.APIModelTestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WeChatConnectionStatusResponseTest extends APIModelTestBase {
    @Test
    public void serializeToJSON() throws Exception {
        final WeChatConnectionStatusResponse response = new WeChatConnectionStatusResponse(true, false);
        assertThat(serializeObject(response))
                .isEqualTo(getResource("fixtures/admin/wechat/WeChatConnectionStatusResponse.json"));
    }
}