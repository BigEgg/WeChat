package com.thoughtworks.wechat_application.resources.exceptions;

import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WeChatMessageAuthenticationExceptionTest {
    @Test
    public void testGeneral() throws Exception {
        WeChatMessageAuthenticationException exception = new WeChatMessageAuthenticationException();
        assertThat(exception.getResponse().getStatus(), equalTo(Response.Status.FORBIDDEN.getStatusCode()));
    }
}