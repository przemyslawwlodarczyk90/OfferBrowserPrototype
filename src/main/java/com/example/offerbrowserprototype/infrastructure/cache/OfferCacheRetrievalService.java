package com.example.offerbrowserprototype.infrastructure.cache;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferCacheRetrievalService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY = "job_offers";

    public OfferCacheRetrievalService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public List<OfferDTO> getCachedOffers() {
        return (List<OfferDTO>) redisTemplate.opsForValue().get(CACHE_KEY);
    }
}
