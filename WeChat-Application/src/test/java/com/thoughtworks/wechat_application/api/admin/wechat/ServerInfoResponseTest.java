package com.thoughtworks.wechat_application.api.admin.wechat;

import com.thoughtworks.wechat_application.api.APIModelTestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerInfoResponseTest extends APIModelTestBase {
    @Test
    public void serializeToJSON() throws Exception {
        final ServerInfoResponse serverInfoResponse = new ServerInfoResponse("http://localhost:3000/wechat", "ABCDE_TOKEN");
        assertThat(serializeObject(serverInfoResponse))
                .isEqualTo(getResource("fixtures/admin/wechat/ServerInfoResponse.json"));
    }
}