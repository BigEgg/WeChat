package com.thoughtworks.wechat_core.util;

import org.junit.Test;

import static com.thoughtworks.wechat_core.util.HashHelper.hash;
import static com.thoughtworks.wechat_core.util.HashHelper.sha1Hash;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class HashHelperTest {
    @Test
    public void testSupportSHA1Hash() throws Exception {
        hash("plain text", "SHA-1");
    }

    @Test
    public void testSHA1Hash() throws Exception {
        final String plainText = "Some data should be hash.";
        final String cipherText = sha1Hash(plainText);
        assertThat(cipherText, notNullValue());
        assertThat(cipherText, equalTo("41CB0DC8C787767E06F189112C2CF97FE3CC71CB"));
    }
}