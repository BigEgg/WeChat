package com.thoughtworks.wechat_core.util;

import org.junit.Test;

import static com.thoughtworks.wechat_core.util.EncryptHelper.md5Encrypt;
import static com.thoughtworks.wechat_core.util.EncryptHelper.sha1Encrypt;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class EncryptHelperTest {
    @Test
    public void testSHA1Encrypt() throws Exception {
        String plainText = "Some data should be encrypted.";
        String cipherText = sha1Encrypt(plainText);
        assertThat(cipherText, notNullValue());
        assertThat(cipherText, equalTo("ED6EDF98D8B496ED379D1E3D0DDE272B1B39EC5B"));
    }

    @Test
    public void testMD5Encrypt() throws Exception {
        String plainText = "Some data should be encrypted.";
        String cipherText = md5Encrypt(plainText);
        assertThat(cipherText, notNullValue());
        assertThat(cipherText, equalTo("CC668D43340C93ECA1C6B75B82CDA540"));
    }
}