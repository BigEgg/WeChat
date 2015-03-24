package com.thoughtworks.wechat_application.resources;

import com.thoughtworks.wechat_application.api.oauth.OAuthRefreshRequest;
import com.thoughtworks.wechat_application.api.oauth.OAuthResponse;
import com.thoughtworks.wechat_application.jdbi.core.AdminUser;
import com.thoughtworks.wechat_application.logic.OAuthProvider;
import com.thoughtworks.wechat_application.models.oauth.AuthenticateRole;
import com.thoughtworks.wechat_application.models.oauth.OAuthInfo;
import com.thoughtworks.wechat_application.services.admin.AdminUserService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OAuthResourceTest extends ResourceTestBase {
    private static final AdminUserService adminUserService = mock(AdminUserService.class);
    private static final OAuthProvider oAuthProvider = mock(OAuthProvider.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new OAuthResource(adminUserService, oAuthProvider))
            .build();

    private static OAuthInfo createOAuthInfo1(AuthenticateRole role) {
        return new OAuthInfo(role, "access_token", "refresh_token", 1000, 1000, DateTime.now());
    }

    private static OAuthInfo createOAuthInfo2(AuthenticateRole role) {
        return new OAuthInfo(role, "access_token2", "refresh_token", 1000, 1000, DateTime.now());
    }

    @After
    public void tearDown() throws Exception {
        reset(adminUserService);
        reset(oAuthProvider);
    }

    @Test
    public void return_token_if_log_in_success() throws Exception {
        when(adminUserService.logIn("username", "password")).thenReturn(Optional.of(mock(AdminUser.class)));
        when(oAuthProvider.newOAuth(AuthenticateRole.ADMIN)).thenReturn(createOAuthInfo1(AuthenticateRole.ADMIN));

        final Response response = resources.client().target("/oauth/admin").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.form(new MultivaluedHashMap<String, String>() {{
            add("username", "username");
            add("password", "password");
        }}));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        final OAuthResponse entity = getResponseEntity(response, OAuthResponse.class);
        assertThat(entity.getAccessToken()).isEqualTo("access_token");
        assertThat(entity.getRefreshToken()).isEqualTo("refresh_token");
    }

    @Test
    public void return_token_if_log_in_failed() throws Exception {
        when(adminUserService.logIn("username", "password")).thenReturn(Optional.empty());
        when(oAuthProvider.newOAuth(AuthenticateRole.ADMIN)).thenReturn(createOAuthInfo1(AuthenticateRole.ADMIN));

        final Response response = resources.client().target("/oauth/admin").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.form(new MultivaluedHashMap<String, String>() {{
            add("username", "username");
            add("password", "password");
        }}));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.UNAUTHORIZED);
    }

    @Test
    public void return_token_if_refresh_success() throws Exception {
        when(oAuthProvider.refreshAccessToken("access_token", "refresh_token")).thenReturn(Optional.of(createOAuthInfo2(AuthenticateRole.ADMIN)));

        final OAuthRefreshRequest request = new OAuthRefreshRequest();
        request.setAccessToken("access_token");
        request.setRefreshToken("refresh_token");
        final Response response = resources.client().target("/oauth/refresh").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        final OAuthResponse entity = getResponseEntity(response, OAuthResponse.class);
        assertThat(entity.getAccessToken()).isEqualTo("access_token2");
        assertThat(entity.getRefreshToken()).isEqualTo("refresh_token");
    }

    @Test
    public void return_token_if_refresh_not_valid() throws Exception {
        when(oAuthProvider.refreshAccessToken("access_token", "refresh_token")).thenReturn(Optional.<OAuthInfo>empty());

        final OAuthRefreshRequest request = new OAuthRefreshRequest();
        request.setAccessToken("access_token");
        request.setRefreshToken("refresh_token");
        final Response response = resources.client().target("/oauth/refresh").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.FORBIDDEN);
    }
}