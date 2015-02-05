package com.thoughtworks.wechat_core.messages;

import org.junit.Test;

import static com.thoughtworks.wechat_core.messages.MessageAuthentication.validation;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class MessageAuthenticationTest {
    @Test
    public void should_pass_validation() throws Exception {
        final String signature = "411ea6a5d9d2f4bc17d82bd56897bd45efe5a3db";
        final String timestamp = "timestamp";
        final String nonce = "nonce";
        final String token = "1a2d202e3e4a5e6c76a7b";

        assertThat(validation(signature, token, timestamp, nonce), equalTo(true));
    }

    @Test
    public void should_not_pass_validation() throws Exception {
        final String signature = "411ea6a5d9d2f4bc17d82bd56897bd45efe5a3db";
        final String timestamp = "timestamp";
        final String nonce = "nonce1";
        final String token = "1a2d202e3e4a5e6c76a7b";

        assertThat(validation(signature, token, timestamp, nonce), equalTo(false));
    }
}