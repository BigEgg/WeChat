package com.thoughtworks.wechat_application.resources.exceptions;

import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WebApplicationNotAcceptableExceptionTest {
    @Test
    public void testGeneral() throws Exception {
        WebApplicationNotAcceptableException exception = new WebApplicationNotAcceptableException();
        assertThat(exception.getResponse().getStatus(), equalTo(Response.Status.NOT_ACCEPTABLE.getStatusCode()));
    }
}