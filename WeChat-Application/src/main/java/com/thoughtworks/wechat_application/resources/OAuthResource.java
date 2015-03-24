package com.thoughtworks.wechat_application.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.api.oauth.OAuthRefreshRequest;
import com.thoughtworks.wechat_application.api.oauth.OAuthResponse;
import com.thoughtworks.wechat_application.jdbi.core.AdminUser;
import com.thoughtworks.wechat_application.logic.OAuthProvider;
import com.thoughtworks.wechat_application.models.oauth.AuthenticateRole;
import com.thoughtworks.wechat_application.models.oauth.OAuthInfo;
import com.thoughtworks.wechat_application.services.admin.AdminUserService;
import org.hibernate.validator.constraints.NotBlank;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Singleton
@Path("/oauth")
@Produces(MediaType.APPLICATION_JSON)
public class OAuthResource {
    private final AdminUserService adminUserService;
    private final OAuthProvider oAuthProvider;

    @Inject
    public OAuthResource(final AdminUserService adminUserService,
                         final OAuthProvider oAuthProvider) {
        this.adminUserService = adminUserService;
        this.oAuthProvider = oAuthProvider;
    }

    @POST
    @Path("/admin")
    public OAuthResponse admin(@FormParam("username") @NotBlank final String username,
                               @FormParam("password") @NotBlank final String password) {
        final Optional<AdminUser> adminUser = adminUserService.logIn(username, password);
        if (adminUser.isPresent()) {
            final OAuthInfo oAuthInfo = oAuthProvider.newOAuth(AuthenticateRole.ADMIN);
            return new OAuthResponse(oAuthInfo.getRefreshToken().get(), oAuthInfo.getAccessToken().get());
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @POST
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    public OAuthResponse refresh(@NotBlank final OAuthRefreshRequest request) {
        final Optional<OAuthInfo> oAuthInfo = oAuthProvider.refreshAccessToken(request.getAccessToken(), request.getRefreshToken());

        if (oAuthInfo.isPresent()) {
            return new OAuthResponse(oAuthInfo.get().getRefreshToken().get(), oAuthInfo.get().getAccessToken().get());
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
