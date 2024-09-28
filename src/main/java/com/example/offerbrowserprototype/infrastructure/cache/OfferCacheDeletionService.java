package com.example.offerbrowserprototype.infrastructure.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class OfferCacheDeletionService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY = "job_offers";

    public OfferCacheDeletionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void clearCache() {
        redisTemplate.delete(CACHE_KEY);
    }
}
