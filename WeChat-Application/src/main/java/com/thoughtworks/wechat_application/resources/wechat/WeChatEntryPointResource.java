package com.thoughtworks.wechat_application.resources.wechat;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowEngine;
import com.thoughtworks.wechat_application.resources.exceptions.WeChatMessageAuthenticationException;
import com.thoughtworks.wechat_application.resources.exceptions.WebApplicationNotAcceptableException;
import com.thoughtworks.wechat_application.services.admin.AdminResourceKey;
import com.thoughtworks.wechat_application.services.admin.AdminResourceService;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageEnvelop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

import static com.thoughtworks.wechat_core.messages.MessageAuthentication.validation;

@Singleton
@Path("/wechat")
public class WeChatEntryPointResource {
    private final Logger LOGGER = LoggerFactory.getLogger(WeChatEntryPointResource.class);
    private final AdminResourceService adminResourceService;
    private final WorkflowEngine workflowEngine;

    @Inject
    public WeChatEntryPointResource(final AdminResourceService adminResourceService,
                                    final WorkflowEngine workflowEngine) {
        this.workflowEngine = workflowEngine;
        this.adminResourceService = adminResourceService;
    }

    @POST
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public OutboundMessageEnvelop handleMessage(@NotNull InboundMessageEnvelop inboundMessageEnvelop,
                                                @QueryParam("signature") String signature,
                                                @QueryParam("timestamp") String timestamp,
                                                @QueryParam("nonce") String nonce) throws Exception {
        validationWeChatCommunication(
                Optional.ofNullable(signature),
                Optional.ofNullable(timestamp),
                Optional.ofNullable(nonce));

        final String fromUser = inboundMessageEnvelop.getFromUser();
        LOGGER.info("[HandleMessage] Get response message(type: {}) from user {};", inboundMessageEnvelop.getMessage().getMessageType(), fromUser);
        final Optional<OutboundMessage> outboundMessage = workflowEngine.handle(inboundMessageEnvelop);
        if (outboundMessage.isPresent()) {
            LOGGER.info("[HandleMessage] Reply user {} with message(type: {}).", fromUser, outboundMessage.get().getMessageType());
        } else {
            LOGGER.info("[HandleMessage] Reply user {} with empty message.", fromUser);
        }

        return inboundMessageEnvelop.reply(outboundMessage);
    }

    @GET
    @Timed
    public String weChatVerify(@QueryParam("signature") String signature,
                               @QueryParam("timestamp") String timestamp,
                               @QueryParam("nonce") String nonce,
                               @QueryParam("echostr") String echoStr) throws Exception {
        validationWeChatCommunication(
                Optional.ofNullable(signature),
                Optional.ofNullable(timestamp),
                Optional.ofNullable(nonce));

        if (echoStr == null) {
            throw new WebApplicationNotAcceptableException();
        } else {
            adminResourceService.setResource(AdminResourceKey.WECHAT_CONNECTION_STATUS, "true");
            return echoStr;
        }
    }

    private void validationWeChatCommunication(Optional<String> signature,
                                               Optional<String> timestamp,
                                               Optional<String> nonce) throws Exception {
        if (!signature.isPresent() || !timestamp.isPresent() || !nonce.isPresent()) {
            throw new WebApplicationNotAcceptableException();
        }

        if (!validation(signature.get(), adminResourceService.getAppToken(), timestamp.get(), nonce.get())) {
            throw new WeChatMessageAuthenticationException();
        }
    }
}