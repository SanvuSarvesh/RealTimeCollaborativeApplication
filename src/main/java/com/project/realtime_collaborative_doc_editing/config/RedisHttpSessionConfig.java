package com.project.realtime_collaborative_doc_editing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800) // 30 minutes
public class RedisHttpSessionConfig {
    // This class enables Redis-backed HTTP sessions

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Configuring the Redis connection factory
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Using String serializer for the key
        template.setKeySerializer(new StringRedisSerializer());

        // Using JSON serializer for the value
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

}
