package com.example.offerbrowserprototype.infrastructure.cache;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
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
