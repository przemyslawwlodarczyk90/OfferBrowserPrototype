package com.example.offerbrowserprototype.infrastructure.cache;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OfferCacheStorageService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${offer.cache.expiration}")
    private long cacheExpirationMinutes;

    public OfferCacheStorageService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheOffers(OfferSource source, List<OfferDTO> offers) {
        redisTemplate.opsForValue().set(CacheKeys.offers(source), offers, cacheExpirationMinutes, TimeUnit.MINUTES);
    }
}
