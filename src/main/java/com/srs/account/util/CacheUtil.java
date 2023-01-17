package com.srs.account.util;

import com.srs.common.cache.json.JsonRedisFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Component
@RequiredArgsConstructor
public class CacheUtil {
    private final JsonRedisFactory redis;

    public void saveQcToken(String token) {
        redis.put("ACCESS_TOKEN", token, Duration.ofMinutes(15));
    }

    public String getQcToken() {
        return redis.getAsMono("ACCESS_TOKEN", String.class);
    }

    public void evictQcToken() {
        redis.evict("ACCESS_TOKEN", String.class);
    }
}
