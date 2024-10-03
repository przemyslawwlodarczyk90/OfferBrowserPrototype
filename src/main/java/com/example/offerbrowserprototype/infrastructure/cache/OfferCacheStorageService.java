package com.example.offerbrowserprototype.infrastructure.cache;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.offerbrowserprototype.infrastructure.cache.CacheKeys.JOB_OFFERS;

@Service
public class OfferCacheStorageService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY = JOB_OFFERS;

    // Pobieranie czasu wygaśnięcia cache'a z konfiguracji
    @Value("${offer.cache.expiration}")
    private long cacheExpirationMinutes;

    public OfferCacheStorageService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheOffers(List<OfferDTO> offers) {
        // Ustawienie czasu wygaśnięcia cache'a na podstawie konfiguracji
        redisTemplate.opsForValue().set(CACHE_KEY, offers, cacheExpirationMinutes, TimeUnit.MINUTES);
    }
}
