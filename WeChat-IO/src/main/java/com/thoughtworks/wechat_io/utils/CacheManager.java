package com.thoughtworks.wechat_io.utils;

import com.google.inject.Singleton;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.thoughtworks.wechat_core.util.precondition.ArgumentPrecondition.checkNotBlank;

@Singleton
public class CacheManager {
    public static final int DEFAULT_CACHE_SECOND = 3600;        //  One Hour
    private final Map<String, CacheObject> caches;

    public CacheManager() {
        caches = new HashMap<>();
    }

    public void put(String key, Object value) {
        put(key, value, DEFAULT_CACHE_SECOND);
    }

    public void put(String key, Object value, int expireSecond) {
        checkNotBlank(key);
        checkNotNull(value);
        checkArgument(expireSecond >= 0);

        caches.put(key, new CacheObject(value, DateTime.now().plusSeconds(expireSecond)));
    }

    public void expire(String key) {
        checkNotBlank(key);

        caches.remove(key);
    }

    public <T> Optional<T> get(String key, Class<T> type) {
        checkNotBlank(key);

        if (caches.containsKey(key)) {
            CacheObject result = caches.get(key);
            if (!result.isExpired() && type.isInstance(result.value)) {
                return Optional.of(type.cast(result.getValue()));
            }
        }
        return Optional.empty();
    }

    protected class CacheObject {
        private final Object value;
        private DateTime expireTime;

        public CacheObject(Object value, DateTime expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }

        public Object getValue() {
            return value;
        }

        public boolean isExpired() {
            return expireTime.isBefore(DateTime.now());
        }
    }
}
