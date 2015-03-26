package com.thoughtworks.wechat_application.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.api.oauth.AdminLoginRequest;
import com.thoughtworks.wechat_application.api.oauth.AdminLoginResponse;
import com.thoughtworks.wechat_application.api.oauth.OAuthRefreshRequest;
import com.thoughtworks.wechat_application.api.oauth.OAuthRefreshResponse;
import com.thoughtworks.wechat_application.jdbi.core.AdminUser;
import com.thoughtworks.wechat_application.logic.OAuthProvider;
import com.thoughtworks.wechat_application.models.oauth.AuthenticateRole;
import com.thoughtworks.wechat_application.models.oauth.OAuthInfo;
import com.thoughtworks.wechat_application.services.admin.AdminUserService;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
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
    public AdminLoginResponse admin(@NotNull AdminLoginRequest request) {
        final String username = request.getUsername();
        final Optional<AdminUser> adminUser = adminUserService.logIn(username, request.getPassword());
        if (adminUser.isPresent()) {
            final OAuthInfo oAuthInfo = oAuthProvider.newOAuth(AuthenticateRole.ADMIN);
            return new AdminLoginResponse(oAuthInfo.getAccessToken().get(), oAuthInfo.getRefreshToken().get(), username.split("@")[0]);
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @POST
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    public OAuthRefreshResponse refresh(@NotBlank final OAuthRefreshRequest request) {
        final Optional<OAuthInfo> oAuthInfo = oAuthProvider.refreshAccessToken(request.getAccessToken(), request.getRefreshToken());

        if (oAuthInfo.isPresent()) {
            return new OAuthRefreshResponse(oAuthInfo.get().getAccessToken().get(), oAuthInfo.get().getRefreshToken().get());
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}
