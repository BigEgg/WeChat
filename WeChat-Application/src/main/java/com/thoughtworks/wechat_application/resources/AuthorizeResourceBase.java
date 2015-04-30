package com.thoughtworks.wechat_application.resources;

import com.thoughtworks.wechat_application.jdbi.core.AuthenticateRole;
import com.thoughtworks.wechat_application.jdbi.core.OAuthClient;
import com.thoughtworks.wechat_application.logic.OAuthProvider;
import org.slf4j.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Optional;

public abstract class AuthorizeResourceBase {
    private final OAuthProvider oAuthProvider;
    protected Logger LOGGER;

    public AuthorizeResourceBase(OAuthProvider oAuthProvider) {
        this.oAuthProvider = oAuthProvider;
    }

    protected void checkAccessToken(final String accessToken, final AuthenticateRole role) {
        final Optional<OAuthClient> oAuthClient = oAuthProvider.getOAuthClient(accessToken);
        if (!oAuthClient.isPresent() || oAuthClient.get().getRole() != role) {
            LOGGER.warn("[CheckAccessToken] The access token '{}' is not valid.", accessToken);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
