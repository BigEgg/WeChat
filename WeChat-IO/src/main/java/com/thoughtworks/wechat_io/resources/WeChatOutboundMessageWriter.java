package com.thoughtworks.wechat_io.resources;

import com.thoughtworks.wechat_core.messages.outbound.OutboundMessageEnvelop;
import com.thoughtworks.wechat_core.wechat.outbound.WeChatOutbound;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_XML)
@Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_XML})
public class WeChatOutboundMessageWriter implements MessageBodyWriter<OutboundMessageEnvelop> {
    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return OutboundMessageEnvelop.class.isAssignableFrom(aClass);
    }

    @Override
    public long getSize(OutboundMessageEnvelop envelop, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(OutboundMessageEnvelop envelop, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        WeChatOutbound outbound = envelop.toWeChat();
        try (final PrintStream printStream = new PrintStream(outputStream)) {
            printStream.print(outbound.toString());
            printStream.close();
        }
    }
}
