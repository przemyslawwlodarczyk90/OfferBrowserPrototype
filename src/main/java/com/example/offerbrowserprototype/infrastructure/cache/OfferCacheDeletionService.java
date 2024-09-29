package com.example.offerbrowserprototype.infrastructure.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static com.example.offerbrowserprototype.infrastructure.cache.CacheKeys.JOB_OFFERS;

@Service
public class OfferCacheDeletionService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY = JOB_OFFERS;

    public OfferCacheDeletionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void clearCache() {
        redisTemplate.delete(CACHE_KEY);
    }
}
