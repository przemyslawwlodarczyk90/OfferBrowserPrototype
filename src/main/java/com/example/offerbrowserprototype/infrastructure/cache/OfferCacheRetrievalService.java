package com.example.offerbrowserprototype.infrastructure.cache;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.offerbrowserprototype.infrastructure.cache.CacheKeys.JOB_OFFERS;

@Service
public class OfferCacheRetrievalService {

    private final RedisTemplate<String, List<OfferDTO>> redisTemplate;
    private static final String CACHE_KEY = JOB_OFFERS;

    public OfferCacheRetrievalService(RedisTemplate<String, List<OfferDTO>> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public List<OfferDTO> getCachedOffers() {
        List<OfferDTO> cachedOffers = redisTemplate.opsForValue().get(CACHE_KEY);
        return cachedOffers != null ? cachedOffers : List.of(); // Obsługa null
    }
}
