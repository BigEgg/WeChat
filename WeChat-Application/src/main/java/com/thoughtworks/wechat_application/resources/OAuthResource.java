package com.thoughtworks.wechat_application.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.api.oauth.OAuthRefreshRequest;
import com.thoughtworks.wechat_application.api.oauth.OAuthResponse;
import com.thoughtworks.wechat_application.api.oauth.OAuthSignInRequest;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import com.thoughtworks.wechat_application.logic.OAuthProvider;
import com.thoughtworks.wechat_application.models.oauth.OAuthInfo;
import com.thoughtworks.wechat_application.services.OAuthClientService;
import com.thoughtworks.wechat_application.services.SystemEventLogService;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Singleton
@Path("/uas/oauth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OAuthResource {
    private final Logger LOGGER = LoggerFactory.getLogger(OAuthResource.class);
    private final OAuthClientService OAuthClientService;
    private final OAuthProvider oAuthProvider;
    private final SystemEventLogService eventLogService;

    @Inject
    public OAuthResource(final OAuthClientService OAuthClientService,
                         final OAuthProvider oAuthProvider,
                         final SystemEventLogService eventLogService) {
        this.OAuthClientService = OAuthClientService;
        this.oAuthProvider = oAuthProvider;
        this.eventLogService = eventLogService;
    }

    @POST
    @Path("/accesstoken")
    public OAuthResponse accessToken(@NotNull OAuthSignInRequest request) {
        final String clientId = request.getClientId();
        final String clientSecret = request.getClientSecret();

        final Optional<OAuthClient> clientOpt = OAuthClientService.SignIn(clientId, clientSecret);
        if (clientOpt.isPresent()) {
            LOGGER.info("[AccessToken] Client with clientId: {} and clientSecret: {} authenticate success.", clientId, clientSecret);

            final OAuthClient client = clientOpt.get();
            final OAuthInfo oAuthInfo = oAuthProvider.newOAuth(client);
            eventLogService.oAuth().accessToken(client, DateTime.now());
            return new OAuthResponse(oAuthInfo.getAccessToken().get(), oAuthInfo.getRefreshToken().get());
        } else {
            LOGGER.info("[AccessToken] Client with clientId: {} and clientSecret: {} authenticate failed.", clientId, clientSecret);
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @POST
    @Path("/refresh")
    public OAuthResponse refresh(@NotBlank final OAuthRefreshRequest request) {
        final String accessToken = request.getAccessToken();
        final String refreshToken = request.getRefreshToken();

        final Optional<OAuthInfo> oAuthInfoOpt = oAuthProvider.refreshAccessToken(accessToken, refreshToken);
        if (oAuthInfoOpt.isPresent()) {
            LOGGER.info("[Refresh] Client refresh access token with old accessToken: {} and refreshToken: {} authenticate success.", accessToken, refreshToken);

            final OAuthInfo oAuthInfo = oAuthInfoOpt.get();
            eventLogService.oAuth().refresh(oAuthInfo.getClient(), DateTime.now());
            return new OAuthResponse(oAuthInfo.getAccessToken().get(), oAuthInfo.getRefreshToken().get());
        } else {
            LOGGER.info("[Refresh] Client refresh access token with accessToken: {} and refreshToken: {} authenticate failed.", accessToken, refreshToken);
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
