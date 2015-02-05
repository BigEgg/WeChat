package com.thoughtworks.wechat_io.resources.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class WeChatMessageAuthenticationException extends WebApplicationException {
    public WeChatMessageAuthenticationException() {
        super(Response.Status.FORBIDDEN);
    }
}
