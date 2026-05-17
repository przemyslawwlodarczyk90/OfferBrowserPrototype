package com.example.offerbrowserprototype.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
@ConditionalOnProperty(value = "spring.cache.type", havingValue = "redis")
public class RedisConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.cache.redis.time-to-live:600000}")
    private long cacheTtlMs;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        logger.info("Initializing RedisConnectionFactory.");
        logger.debug("Redis host: {}", redisHost);
        logger.debug("Redis port: {}", redisPort);

        try {
            RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
            configuration.setHostName(redisHost);
            configuration.setPort(redisPort);

            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(configuration);
            logger.info("RedisConnectionFactory initialized successfully.");
            return jedisConnectionFactory;
        } catch (Exception e) {
            logger.error("Error initializing RedisConnectionFactory", e);
            throw e;
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        logger.info("Initializing RedisTemplate.");

        try {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);

            // Configure the serializer for keys and values
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(valueSerializer);
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(valueSerializer);

            logger.info("RedisTemplate initialized successfully.");
            return template;
        } catch (Exception e) {
            logger.error("Error initializing RedisTemplate", e);
            throw e;
        }
    }

    @Bean
    public RedisSerializer<Object> redisSerializer() {
        logger.info("Initializing RedisSerializer.");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
            logger.info("RedisSerializer initialized successfully.");
            return serializer;
        } catch (Exception e) {
            logger.error("Error initializing RedisSerializer", e);
            throw e;
        }
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMillis(cacheTtlMs))
                .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(SerializationPair.fromSerializer(redisSerializer()))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}
