package com.thoughtworks.wechat_application.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CacheManagerTest {
    @Test
    public void testInject() {
        final Injector injector = Guice.createInjector(binder -> {
        });

        final CacheManager cacheManager = injector.getInstance(CacheManager.class);
        assertThat(cacheManager, notNullValue());
    }

    @Test
    public void testInject_Singleton() {
        final Injector injector = Guice.createInjector(binder -> {
        });

        final CacheManager cacheManager = injector.getInstance(CacheManager.class);
        final CacheManager anotherCacheManager = injector.getInstance(CacheManager.class);
        assertThat(cacheManager, equalTo(anotherCacheManager));
    }

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