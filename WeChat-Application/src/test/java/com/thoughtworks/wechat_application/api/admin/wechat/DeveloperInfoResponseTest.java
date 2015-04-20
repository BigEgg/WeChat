package com.thoughtworks.wechat_application.api.admin.wechat;

import com.thoughtworks.wechat_application.api.APIModelTestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeveloperInfoResponseTest extends APIModelTestBase {
    @Test
    public void serializeToJSON() throws Exception {
        final DeveloperInfoResponse developerInfoResponse = new DeveloperInfoResponse("app_id", "app_secret");
        assertThat(serializeObject(developerInfoResponse))
                .isEqualTo(getResource("fixtures/admin/wechat/DeveloperInfoResponse.json"));
    }
}
