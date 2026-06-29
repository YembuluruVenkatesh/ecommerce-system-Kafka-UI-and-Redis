package com.example.inventory.config;

import com.example.inventory.entity.Inventory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Inventory> redisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Inventory> template =
                new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        // Serializer for Redis Keys
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Serializer for Redis Values
        Jackson2JsonRedisSerializer<Inventory> serializer =
                new Jackson2JsonRedisSerializer<>(Inventory.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        serializer.setObjectMapper(mapper);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();

        return template;
    }
}