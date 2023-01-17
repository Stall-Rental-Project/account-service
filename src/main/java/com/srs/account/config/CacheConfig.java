package com.srs.account.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srs.common.cache.json.JsonRedisFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class CacheConfig {
    @Bean
    public JsonRedisFactory jsonRedisFactory(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        return new JsonRedisFactory(factory, objectMapper);
    }
}
