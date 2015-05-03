package com.thoughtworks.wechat_application.resources.admin.basicSettings;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.api.admin.wechat.DeveloperInfoResponse;
import com.thoughtworks.wechat_application.api.admin.wechat.NewDeveloperInfoRequest;
import com.thoughtworks.wechat_application.api.admin.wechat.ServerInfoResponse;
import com.thoughtworks.wechat_application.api.admin.wechat.WeChatConnectionStatusResponse;
import com.thoughtworks.wechat_application.jdbi.core.AuthenticateRole;
import com.thoughtworks.wechat_application.logic.OAuthProvider;
import com.thoughtworks.wechat_application.resources.AuthorizeResourceBase;
import com.thoughtworks.wechat_application.resources.wechat.WeChatEntryPointResource;
import com.thoughtworks.wechat_application.services.admin.AdminResourceService;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

@Singleton
@Path("/api/admin/wechat")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WeChatBasicSettingsResource extends AuthorizeResourceBase {
    private final AdminResourceService adminResourceService;

    @Inject
    public WeChatBasicSettingsResource(final AdminResourceService adminResourceService,
                                       final OAuthProvider oAuthProvider) {
        super(oAuthProvider);

        this.adminResourceService = adminResourceService;
        LOGGER = LoggerFactory.getLogger(WeChatBasicSettingsResource.class);
    }

    @GET
    @Path("/status")
    public WeChatConnectionStatusResponse getStatus(@QueryParam("access_token") final String accessToken) {
        checkAccessToken(accessToken, AuthenticateRole.ADMIN);

        return new WeChatConnectionStatusResponse(adminResourceService.weChat().getConnectionStatus(), adminResourceService.weChat().getAPIStatus());
    }

    @GET
    @Path("/server")
    public ServerInfoResponse getServerInformation(@Context final HttpServletRequest request,
                                                   @QueryParam("access_token") final String accessToken) {
        checkAccessToken(accessToken, AuthenticateRole.ADMIN);

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

        return new ServerInfoResponse(url.toString(), adminResourceService.weChat().getAppToken());
    }

    @GET
    @Path("/developer")
    public DeveloperInfoResponse getDeveloperInfo(@QueryParam("access_token") final String accessToken) {
        checkAccessToken(accessToken, AuthenticateRole.ADMIN);

        return new DeveloperInfoResponse(adminResourceService.weChat().getAppId(), adminResourceService.weChat().getAppSecret());
    }

    @PUT
    @Path("/developer")
    public DeveloperInfoResponse setDeveloperInfo(@QueryParam("access_token") final String accessToken,
                                                  @NotNull final NewDeveloperInfoRequest newDeveloperInfoRequest) {
        checkAccessToken(accessToken, AuthenticateRole.ADMIN);

        adminResourceService.weChat().setAppId(newDeveloperInfoRequest.getAppId());
        adminResourceService.weChat().setAppSecret(newDeveloperInfoRequest.getAppSecret());
        return new DeveloperInfoResponse(adminResourceService.weChat().getAppId(), adminResourceService.weChat().getAppSecret());
    }
}
