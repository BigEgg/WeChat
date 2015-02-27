package com.thoughtworks.wechat_io.utils;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CacheManagerTest {
    @Test
    public void testPutGet() throws Exception {
        final String key = "key";
        final CacheManager cacheManager = new CacheManager();
        cacheManager.put(key, "Value");

        final Optional<String> value = cacheManager.get(key, String.class);
        assertThat(value.isPresent(), equalTo(true));
        assertThat(value.get(), equalTo("Value"));
    }

    @Test
    public void testPutGet_ManualExpire() throws Exception {
        final String key = "key";
        final CacheManager cacheManager = new CacheManager();
        cacheManager.put(key, "Value");

        cacheManager.expire(key);
        final Optional<String> value = cacheManager.get(key, String.class);
        assertThat(value.isPresent(), equalTo(false));
    }

    @Test
    public void testPutGet_AutoExpire() throws Exception {
        final String key = "key";
        final CacheManager cacheManager = new CacheManager();
        cacheManager.put(key, "Value", 1);

        Thread.sleep(1000L);

        final Optional<String> value = cacheManager.get(key, String.class);
        assertThat(value.isPresent(), equalTo(false));
    }
}