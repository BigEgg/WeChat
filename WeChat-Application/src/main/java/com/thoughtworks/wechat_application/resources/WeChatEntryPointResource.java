package com.thoughtworks.wechat_application.resources;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageEnvelop;
import com.thoughtworks.wechat_application.configs.WeChatConfiguration;
import com.thoughtworks.wechat_application.resources.exceptions.WeChatMessageAuthenticationException;
import com.thoughtworks.wechat_application.resources.exceptions.WebApplicationNotAcceptableException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Optional;

import static com.thoughtworks.wechat_core.messages.MessageAuthentication.validation;

@Singleton
@Path("/wechat")
public class WeChatEntryPointResource {
    private final String appToken;

    @Inject
    public WeChatEntryPointResource(WeChatConfiguration config) {
        appToken = config.getAppToken();
    }

    @POST
    public OutboundMessageEnvelop handleMessage(InboundMessageEnvelop inboundMessageEnvelop,
                                                @QueryParam("signature") Optional<String> signature,
                                                @QueryParam("timestamp") Optional<String> timestamp,
                                                @QueryParam("nonce") Optional<String> nonce) throws Exception {
        validationWeChatCommunication(signature, timestamp, nonce);


        throw new NotImplementedException();
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