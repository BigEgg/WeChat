package com.thoughtworks.wechat_application.resources.admin;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.api.admin.wechat.DeveloperInfoResponse;
import com.thoughtworks.wechat_application.api.admin.wechat.ServerInfoResponse;
import com.thoughtworks.wechat_application.jdbi.core.AuthenticateRole;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import com.thoughtworks.wechat_application.logic.OAuthProvider;
import com.thoughtworks.wechat_application.resources.wechat.WeChatEntryPointResource;
import com.thoughtworks.wechat_application.services.admin.AdminResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Optional;

@Singleton
@Path("/api/admin/wechat")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WeChatSettingsResource {
    private final static Logger LOGGER = LoggerFactory.getLogger(WeChatSettingsResource.class);
    private final AdminResourceService adminResourceService;
    private final OAuthProvider oAuthProvider;

    @Inject
    public WeChatSettingsResource(final AdminResourceService adminResourceService,
                                  final OAuthProvider oAuthProvider) {
        this.adminResourceService = adminResourceService;
        this.oAuthProvider = oAuthProvider;
    }

    @GET
    @Path("/server")
    public ServerInfoResponse getServerInformation(@Context final HttpServletRequest request,
                                                   @QueryParam("access_token") final String accessToken) {
        checkAccessToken(accessToken);

        StringBuilder url = new StringBuilder();
        if (request != null) {
            String scheme = request.getScheme();             // http
            String serverName = request.getServerName();     // hostname.com
            int serverPort = request.getServerPort();        // 80

            url.append(scheme).append("://").append(serverName);
            if ((serverPort != 80) && (serverPort != 443)) {
                url.append(":").append(serverPort);
            }
        }

        final String entryPointPath = UriBuilder.fromResource(WeChatEntryPointResource.class).build().toASCIIString();
        url.append(entryPointPath);

        return new ServerInfoResponse(url.toString(), adminResourceService.getAppToken(), adminResourceService.getConnectionStatus());
    }

    @GET
    @Path("/developer")
    public DeveloperInfoResponse getDeveloperInfo(@QueryParam("access_token") final String accessToken) {
        checkAccessToken(accessToken);

        return new DeveloperInfoResponse(adminResourceService.getAppId(), adminResourceService.getAppSecret());
    }

    private void checkAccessToken(final String accessToken) {
        final Optional<OAuthClient> oAuthClient = oAuthProvider.getOAuthClient(accessToken);
        if (!oAuthClient.isPresent() || oAuthClient.get().getRole() != AuthenticateRole.ADMIN) {
            LOGGER.warn("[CheckAccessToken] The access token '{}' is not valid.", accessToken);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
