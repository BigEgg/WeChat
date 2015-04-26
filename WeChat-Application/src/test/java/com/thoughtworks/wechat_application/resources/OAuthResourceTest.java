package com.thoughtworks.wechat_application.resources;

import com.thoughtworks.wechat_application.api.oauth.OAuthRefreshRequest;
import com.thoughtworks.wechat_application.api.oauth.OAuthResponse;
import com.thoughtworks.wechat_application.api.oauth.OAuthSignInRequest;
import com.thoughtworks.wechat_application.jdbi.core.AuthenticateRole;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import com.thoughtworks.wechat_application.logic.OAuthProvider;
import com.thoughtworks.wechat_application.models.oauth.OAuthInfo;
import com.thoughtworks.wechat_application.services.OAuthClientService;
import com.thoughtworks.wechat_application.services.SystemEventLogService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OAuthResourceTest extends ResourceTestBase {
    private static final OAuthClientService oAuthClientService = mock(OAuthClientService.class);
    private static final OAuthProvider oAuthProvider = mock(OAuthProvider.class);
    private static final SystemEventLogService eventLogService = mock(SystemEventLogService.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new OAuthResource(oAuthClientService, oAuthProvider, eventLogService))
            .build();

    private static OAuthClient createAdmin() {
        return new OAuthClient(1L, "clientId", "hashedClientSecret", AuthenticateRole.ADMIN, Optional.<Long>empty());
    }

    private static OAuthInfo createOAuthInfo1() {
        return new OAuthInfo("access_token", "refresh_token", createAdmin(), 1000, 1000, DateTime.now());
    }

    private static OAuthInfo createOAuthInfo2() {
        return new OAuthInfo("access_token2", "refresh_token", createAdmin(), 1000, 1000, DateTime.now());
    }

    @After
    public void tearDown() throws Exception {
        reset(oAuthClientService);
        reset(oAuthProvider);
        reset(eventLogService);
    }

    @Test
    public void return_token_if_log_in_success() throws Exception {
        final OAuthClient admin = createAdmin();
        when(oAuthClientService.SignIn("abc@abc.com", "password")).thenReturn(Optional.of(admin));
        when(oAuthProvider.newOAuth(admin)).thenReturn(createOAuthInfo1());
        when(eventLogService.oAuth()).thenReturn(mock(SystemEventLogService.OAuthSystemEventLogService.class));

        final OAuthSignInRequest request = new OAuthSignInRequest();
        request.setClientId("abc@abc.com");
        request.setClientSecret("password");
        final Response response = resources.client().target("/uas/oauth/accesstoken").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);

        final OAuthResponse entity = getResponseEntity(response, OAuthResponse.class);
        assertThat(entity.getAccessToken()).isEqualTo("access_token");
        assertThat(entity.getRefreshToken()).isEqualTo("refresh_token");

        verify(eventLogService).oAuth();
        verify(eventLogService.oAuth()).accessToken(any(OAuthClient.class), any(DateTime.class));
    }

    @Test
    public void return_token_if_log_in_failed() throws Exception {
        when(oAuthClientService.SignIn("abc@abc.com", "password")).thenReturn(Optional.empty());
        when(oAuthProvider.newOAuth(createAdmin())).thenReturn(createOAuthInfo1());

        final OAuthSignInRequest request = new OAuthSignInRequest();
        request.setClientId("abc@abc.com");
        request.setClientSecret("password");
        final Response response = resources.client().target("/uas/oauth/accesstoken").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);

        final OAuthResponse entity = getResponseEntity(response, OAuthResponse.class);
        assertThat(entity.getAccessToken()).isEqualTo("");
        assertThat(entity.getRefreshToken()).isEqualTo("");
    }

    @Test
    public void return_token_if_refresh_success() throws Exception {
        when(oAuthProvider.refreshAccessToken("access_token", "refresh_token")).thenReturn(Optional.of(createOAuthInfo2()));
        when(eventLogService.oAuth()).thenReturn(mock(SystemEventLogService.OAuthSystemEventLogService.class));

        final OAuthRefreshRequest request = new OAuthRefreshRequest();
        request.setAccessToken("access_token");
        request.setRefreshToken("refresh_token");

        final Response response = resources.client().target("/uas/oauth/refresh").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);

        final OAuthResponse entity = getResponseEntity(response, OAuthResponse.class);
        assertThat(entity.getAccessToken()).isEqualTo("access_token2");
        assertThat(entity.getRefreshToken()).isEqualTo("refresh_token");

        verify(eventLogService).oAuth();
        verify(eventLogService.oAuth()).refresh(any(OAuthClient.class), any(DateTime.class));
    }

    @Test
    public void return_token_if_refresh_not_valid() throws Exception {
        when(oAuthProvider.refreshAccessToken("access_token", "refresh_token")).thenReturn(Optional.<OAuthInfo>empty());

        final OAuthRefreshRequest request = new OAuthRefreshRequest();
        request.setAccessToken("access_token");
        request.setRefreshToken("refresh_token");

        final Response response = resources.client().target("/uas/oauth/refresh").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);

        final OAuthResponse entity = getResponseEntity(response, OAuthResponse.class);
        assertThat(entity.getAccessToken()).isEqualTo("");
        assertThat(entity.getRefreshToken()).isEqualTo("");
    }
}