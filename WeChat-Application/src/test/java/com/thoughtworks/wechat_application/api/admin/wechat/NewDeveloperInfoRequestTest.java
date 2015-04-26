package com.thoughtworks.wechat_application.api.admin.wechat;

import com.thoughtworks.wechat_application.api.APIModelTestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NewDeveloperInfoRequestTest extends APIModelTestBase {
    @Test
    public void deserializeFromJSON() throws Exception {
        NewDeveloperInfoRequest request = deserializeFixture("fixtures/admin/wechat/NewDeveloperInfoRequest.json", NewDeveloperInfoRequest.class);
        assertThat(request.getAppId()).isEqualTo("app_id");
        assertThat(request.getAppSecret()).isEqualTo("app_secret");
    }
}
