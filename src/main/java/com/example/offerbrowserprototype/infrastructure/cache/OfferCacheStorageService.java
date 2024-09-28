package com.example.offerbrowserprototype.infrastructure.cache;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.offerbrowserprototype.infrastructure.cache.CacheKeys.JOB_OFFERS;

@Service
public class OfferCacheStorageService {

    private final RedisTemplate<String, List<OfferDTO>> redisTemplate;
    private static final String CACHE_KEY = JOB_OFFERS;

    public OfferCacheStorageService(RedisTemplate<String, List<OfferDTO>> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheOffers(List<OfferDTO> offers) {
        redisTemplate.opsForValue().set(CACHE_KEY, offers, 10, TimeUnit.MINUTES);
    }
}
