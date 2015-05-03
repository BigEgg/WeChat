package com.thoughtworks.wechat_application.resources.wechat;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.wechat_application.logic.workflow.WorkflowEngine;
import com.thoughtworks.wechat_application.resources.exceptions.WeChatMessageAuthenticationException;
import com.thoughtworks.wechat_application.resources.exceptions.WebApplicationNotAcceptableException;
import com.thoughtworks.wechat_application.services.admin.AdminResourceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

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
        private final WorkflowEngine workflowEngine = mock(WorkflowEngine.class);
        private WeChatEntryPointResource resource;
        private Injector injector;

        @Before
        public void setUp() throws Exception {
            when(adminResourceService.weChat()).thenReturn(mock(AdminResourceService.WeChatService.class));
            when(adminResourceService.weChat().getAppToken()).thenReturn(token);

            injector = Guice.createInjector(binder -> {
                binder.bind(AdminResourceService.class).toInstance(adminResourceService);
                binder.bind(WorkflowEngine.class).toInstance(workflowEngine);
            });

            resource = injector.getInstance(WeChatEntryPointResource.class);
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_signature_empty() throws Exception {
            resource.weChatVerify(null, timestamp, nonce, echoString);
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_timestamp_empty() throws Exception {
            resource.weChatVerify(signature, null, nonce, echoString);
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_nonce_empty() throws Exception {
            resource.weChatVerify(signature, timestamp, null, echoString);
        }

        @Test(expected = WeChatMessageAuthenticationException.class)
        public void should_throw_exception_wechat_authentication_failed() throws Exception {
            resource.weChatVerify(wrongSignature, timestamp, nonce, echoString);
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_echo_empty() throws Exception {
            resource.weChatVerify(signature, timestamp, nonce, null);
        }

        @Test
        public void should_return_echo_string() throws Exception {
            resource.weChatVerify(signature, timestamp, nonce, echoString);

            verify(adminResourceService.weChat()).setConnectionStatus(eq("true"));
        }
    }

    public static class when_handle_message_with_wrong_authentication {
        private final AdminResourceService adminResourceService = mock(AdminResourceService.class);
        private final WorkflowEngine workflowEngine = mock(WorkflowEngine.class);
        private WeChatEntryPointResource resource;
        private Injector injector;

        @Before
        public void setUp() throws Exception {
            when(adminResourceService.weChat()).thenReturn(mock(AdminResourceService.WeChatService.class));
            when(adminResourceService.weChat().getAppToken()).thenReturn(token);

            injector = Guice.createInjector(binder -> {
                binder.bind(AdminResourceService.class).toInstance(adminResourceService);
                binder.bind(WorkflowEngine.class).toInstance(workflowEngine);
            });

            resource = injector.getInstance(WeChatEntryPointResource.class);
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_signature_empty() throws Exception {
            resource.handleMessage(null, null, timestamp, nonce);
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_timestamp_empty() throws Exception {
            resource.handleMessage(null, signature, null, nonce);
        }

        @Test(expected = WebApplicationNotAcceptableException.class)
        public void should_throw_exception_when_nonce_empty() throws Exception {
            resource.handleMessage(null, signature, timestamp, null);
        }
    }
}