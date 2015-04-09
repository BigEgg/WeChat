package com.thoughtworks.wechat_application.resources.admin;

import com.thoughtworks.wechat_application.jdbi.core.AuthenticateRole;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import com.thoughtworks.wechat_application.logic.OAuthProvider;
import com.thoughtworks.wechat_application.resources.ResourceTestBase;
import com.thoughtworks.wechat_application.services.admin.AdminResourceService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class WeChatSettingsResourceTest extends ResourceTestBase {
    private final static AdminResourceService adminResourceService = mock(AdminResourceService.class);
    private final static OAuthProvider oAuthProvider = mock(OAuthProvider.class);

    @ClassRule
    public final static ResourceTestRule resource = ResourceTestRule.builder()
            .addResource(new WeChatSettingsResource(adminResourceService, oAuthProvider))
            .build();

    private static OAuthClient createAdmin() {
        return new OAuthClient(1L, "clientId", "hashedClientSecret", AuthenticateRole.ADMIN, Optional.<Long>empty());
    }

    private static OAuthClient createVendor() {
        return new OAuthClient(1L, "clientId", "hashedClientSecret", AuthenticateRole.VENDOR, Optional.<Long>empty());
    }

    @After
    public void tearDown() throws Exception {
        reset(adminResourceService);
        reset(oAuthProvider);
    }

    @Test
    public void return_wechat_entry_point_if_access_token_valid() throws Exception {
        when(oAuthProvider.getOAuthClient("accessToken")).thenReturn(Optional.of(createAdmin()));

        final Response response = resource.client().target("/api/admin/wechat/entrypoint").queryParam("access_token", "accessToken").request().get();
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);

        final String url = getResponseJson(response);
        assertThat(url).isEqualTo("/wechat");

        verify(oAuthProvider).getOAuthClient(eq("accessToken"));
    }

    @Test
    public void throw_forbidden_if_access_token_failed() throws Exception {
        when(oAuthProvider.getOAuthClient("accessToken")).thenReturn(Optional.<OAuthClient>empty());

        final Response response = resource.client().target("/api/admin/wechat/entrypoint").queryParam("access_token", "accessToken").request().get();
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.FORBIDDEN);
    }

    @Test
    public void throw_forbidden_if_access_token_not_admin() throws Exception {
        when(oAuthProvider.getOAuthClient("accessToken")).thenReturn(Optional.of(createVendor()));

        final Response response = resource.client().target("/api/admin/wechat/entrypoint").queryParam("access_token", "accessToken").request().get();
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.FORBIDDEN);
    }
}