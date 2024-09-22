package com.example.offerbrowserprototype.infrastructure.cache;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OfferCacheService {

    private final RedisTemplate<String, List<OfferDTO>> redisTemplate;
    private static final String CACHE_KEY = "job_offers";

    public OfferCacheService(RedisTemplate<String, List<OfferDTO>> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheOffers(List<OfferDTO> offers) {
        redisTemplate.opsForValue().set(CACHE_KEY, offers, 10, TimeUnit.MINUTES);
    }

    public List<OfferDTO> getCachedOffers() {
        return redisTemplate.opsForValue().get(CACHE_KEY);
    }
}
