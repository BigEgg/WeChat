package com.thoughtworks.wechat_application.resources;

import com.thoughtworks.wechat_core.messages.inbound.InboundMessageEnvelop;
import com.thoughtworks.wechat_core.wechat.inbound.WeChatInbound;
import com.thoughtworks.wechat_core.wechat.inbound.WeChatInboundDeserializer;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Optional;

@Provider
@Produces(MediaType.APPLICATION_XML)
@Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_XML})
public class WeChatInboundMessageReader implements MessageBodyReader<InboundMessageEnvelop> {
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return InboundMessageEnvelop.class.isAssignableFrom(aClass);
    }

    @Override
    public InboundMessageEnvelop readFrom(Class<InboundMessageEnvelop> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        Optional<WeChatInbound> weChatInboundOpt = WeChatInboundDeserializer.tryDeserialize(convertStreamToString(inputStream));
        if (weChatInboundOpt.isPresent()) {
            return weChatInboundOpt.get().toEnvelop();
        }
        return null;
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
