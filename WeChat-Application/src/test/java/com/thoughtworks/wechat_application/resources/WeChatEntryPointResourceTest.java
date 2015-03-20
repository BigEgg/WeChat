package com.thoughtworks.wechat_application.resources;

import com.thoughtworks.wechat_application.resources.exceptions.WeChatMessageAuthenticationException;
import com.thoughtworks.wechat_application.resources.exceptions.WebApplicationNotAcceptableException;
import com.thoughtworks.wechat_application.services.admin.AdminResourceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class WeChatEntryPointResourceTest {
    private final static String signature = "411ea6a5d9d2f4bc17d82bd56897bd45efe5a3db";
    private final static String wrongSignature = "411ea6a5d9d2f4bc17d82bd56897bd45efe5a3db123";
    private final static String timestamp = "timestamp";
    private final static String nonce = "nonce";
    private final static String echoString = "echo";
    private final static String token = "1a2d202e3e4a5e6c76a7b";

    public static class when_verify_wechat_authentication {
        private final AdminResourceService adminResourceService = mock(AdminResourceService.class);
        private WeChatEntryPointResource resource;

        @Before
        public void setUp() throws Exception {
            when(adminResourceService.getAppToken()).thenReturn(token);
            resource = new WeChatEntryPointResource(adminResourceService);
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_signature_empty() throws Exception {
            resource.weChatVerify(Optional.empty(), Optional.of(timestamp), Optional.of(nonce), Optional.of(echoString));
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_timestamp_empty() throws Exception {
            resource.weChatVerify(Optional.of(signature), Optional.empty(), Optional.of(nonce), Optional.of(echoString));
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_nonce_empty() throws Exception {
            resource.weChatVerify(Optional.of(signature), Optional.of(timestamp), Optional.empty(), Optional.of(echoString));
        }

        @Test(expected = WeChatMessageAuthenticationException.class)
        public void should_throw_exception_wechat_authentication_failed() throws Exception {
            resource.weChatVerify(Optional.of(wrongSignature), Optional.of(timestamp), Optional.of(nonce), Optional.of(echoString));
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_echo_empty() throws Exception {
            resource.weChatVerify(Optional.of(signature), Optional.of(timestamp), Optional.of(nonce), Optional.empty());
        }

        @Test
        public void should_return_echo_string() throws Exception {
            resource.weChatVerify(Optional.of(signature), Optional.of(timestamp), Optional.of(nonce), Optional.of(echoString));
        }
    }

    public static class when_handle_message {
        private final AdminResourceService adminResourceService = mock(AdminResourceService.class);
        private WeChatEntryPointResource resource;

        @Before
        public void setUp() throws Exception {
            when(adminResourceService.getAppToken()).thenReturn(token);
            resource = new WeChatEntryPointResource(adminResourceService);
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_signature_empty() throws Exception {
            resource.handleMessage(null, Optional.empty(), Optional.of(timestamp), Optional.of(nonce));
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_timestamp_empty() throws Exception {
            resource.handleMessage(null, Optional.of(signature), Optional.empty(), Optional.of(nonce));
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_nonce_empty() throws Exception {
            resource.handleMessage(null, Optional.of(signature), Optional.of(timestamp), Optional.empty());
        }
    }
}