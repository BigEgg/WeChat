package com.thoughtworks.wechat_application.resources.admin.basicSettings;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.logic.OAuthProvider;
import com.thoughtworks.wechat_application.resources.AuthorizeResourceBase;
import com.thoughtworks.wechat_application.services.admin.AdminResourceService;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/api/admin/systemmessage")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SystemMessageBasicSettingsResource extends AuthorizeResourceBase {
    private final AdminResourceService adminResourceService;

    @Inject
    public SystemMessageBasicSettingsResource(final AdminResourceService adminResourceService, final OAuthProvider oAuthProvider) {
        super(oAuthProvider);

        this.adminResourceService = adminResourceService;
        LOGGER = LoggerFactory.getLogger(SystemMessageBasicSettingsResource.class);
    }
}
