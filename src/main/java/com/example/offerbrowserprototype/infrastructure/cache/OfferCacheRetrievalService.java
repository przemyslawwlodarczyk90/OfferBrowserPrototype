package com.example.offerbrowserprototype.infrastructure.cache;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferCacheRetrievalService {

    private final RedisTemplate<String, Object> redisTemplate;

    public OfferCacheRetrievalService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @SuppressWarnings("unchecked")
    public List<OfferDTO> getCachedOffers(OfferSource source) {
        Object cachedOffers = redisTemplate.opsForValue().get(CacheKeys.offers(source));
        return cachedOffers != null ? (List<OfferDTO>) cachedOffers : List.of();
    }
}
