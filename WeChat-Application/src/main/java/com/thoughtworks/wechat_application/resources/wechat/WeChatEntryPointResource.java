package com.thoughtworks.wechat_application.resources.wechat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowEngine;
import com.thoughtworks.wechat_application.resources.exceptions.WeChatMessageAuthenticationException;
import com.thoughtworks.wechat_application.resources.exceptions.WebApplicationNotAcceptableException;
import com.thoughtworks.wechat_application.services.admin.AdminResourceService;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessage;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageEnvelop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

import static com.thoughtworks.wechat_core.messages.MessageAuthentication.validation;

@Singleton
@Path("/wechat")
@Produces(MediaType.APPLICATION_XML)
@Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_XML})
public class WeChatEntryPointResource {
    private final Logger LOGGOR = LoggerFactory.getLogger(WeChatEntryPointResource.class);
    private final String appToken;
    private final WorkflowEngine workflowEngine;

    @Inject
    public WeChatEntryPointResource(final AdminResourceService adminResourceService,
                                    final WorkflowEngine workflowEngine) {
        this.workflowEngine = workflowEngine;
        appToken = adminResourceService.getAppToken();
    }

    @POST
    public OutboundMessageEnvelop handleMessage(InboundMessageEnvelop inboundMessageEnvelop,
                                                @QueryParam("signature") Optional<String> signature,
                                                @QueryParam("timestamp") Optional<String> timestamp,
                                                @QueryParam("nonce") Optional<String> nonce) throws Exception {
        validationWeChatCommunication(signature, timestamp, nonce);

        final String fromUser = inboundMessageEnvelop.getFromUser();
        LOGGOR.info("[HandleMessage] Get response message(type: {}) from user {};", inboundMessageEnvelop.getMessage().getMessageType(), fromUser);
        final Optional<OutboundMessage> outboundMessage = workflowEngine.handle(inboundMessageEnvelop);
        if (outboundMessage.isPresent()) {
            LOGGOR.info("[HandleMessage] Reply user {} with message(type: {}).", fromUser, outboundMessage.get().getMessageType());
        } else {
            LOGGOR.info("[HandleMessage] Reply user {} with empty message.", fromUser);
        }

        return inboundMessageEnvelop.reply(outboundMessage);
    }

    @GET
    public String weChatVerify(@QueryParam("signature") Optional<String> signature,
                               @QueryParam("timestamp") Optional<String> timestamp,
                               @QueryParam("nonce") Optional<String> nonce,
                               @QueryParam("echostr") Optional<String> echoStr) throws Exception {
        validationWeChatCommunication(signature, timestamp, nonce);

        return echoStr.orElseThrow(WebApplicationNotAcceptableException::new);
    }

    private void validationWeChatCommunication(Optional<String> signature,
                                               Optional<String> timestamp,
                                               Optional<String> nonce) throws Exception {
        if (!signature.isPresent() || !timestamp.isPresent() || !nonce.isPresent()) {
            throw new WebApplicationNotAcceptableException();
        }

        if (!validation(signature.get(), appToken, timestamp.get(), nonce.get())) {
            throw new WeChatMessageAuthenticationException();
        }
    }
}