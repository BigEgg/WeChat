package com.thoughtworks.wechat_core.util;

import org.junit.Test;

import static com.thoughtworks.wechat_core.util.HashHelper.sha1Hash;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class HashHelperTest {
    @Test
    public void testSHA1Encrypt() throws Exception {
        String plainText = "Some data should be hash.";
        String cipherText = sha1Hash(plainText);
        assertThat(cipherText, notNullValue());
        assertThat(cipherText, equalTo("ED6EDF98D8B496ED379D1E3D0DDE272B1B39EC5B"));
    }
}