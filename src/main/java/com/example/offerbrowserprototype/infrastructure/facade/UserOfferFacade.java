package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.dto.useroffer.UserOfferStatusDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.domain.mapper.UserOfferStatusMapper;
import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.usseroffer.UserAppliedOffersCountHandler;
import com.example.offerbrowserprototype.domain.usseroffer.UserAppliedOffersHandler;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferApplyHandler;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferQueryHandler;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class UserOfferFacade {

    private final UserOfferApplyHandler applyHandler;
    private final UserRepository userRepository;
    private final UserOfferStatusMapper userOfferStatusMapper;
    private final OfferMapper offerMapper;
    private final UserOfferQueryHandler queryHandler;
    private final UserAppliedOffersHandler appliedOffersHandler;
    private final UserAppliedOffersCountHandler appliedOffersCountHandler;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String APPLIED_OFFERS_CACHE_KEY_PREFIX = "userOffers:applied:";
    private static final String APPLIED_OFFERS_COUNT_CACHE_KEY_PREFIX = "userOffers:appliedCount:";

    public UserOfferFacade(UserOfferApplyHandler applyHandler,
                           UserRepository userRepository,
                           UserOfferStatusMapper userOfferStatusMapper,
                           OfferMapper offerMapper,
                           UserOfferQueryHandler queryHandler,
                           UserAppliedOffersHandler appliedOffersHandler,
                           UserAppliedOffersCountHandler appliedOffersCountHandler,
                           RedisTemplate<String, Object> redisTemplate) {
        this.applyHandler = applyHandler;
        this.userRepository = userRepository;
        this.userOfferStatusMapper = userOfferStatusMapper;
        this.offerMapper = offerMapper;
        this.queryHandler = queryHandler;
        this.appliedOffersHandler = appliedOffersHandler;
        this.appliedOffersCountHandler = appliedOffersCountHandler;
        this.redisTemplate = redisTemplate;
    }

    public UserOfferStatusDTO applyToOfferByEmail(String email, String offerId) {
        String userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"))
                .getId();

        var userOfferStatus = applyHandler.applyToOffer(userId, offerId);

        // Usunięcie cache'owanych danych po zmianie stanu aplikacji użytkownika
        String appliedOffersCacheKey = APPLIED_OFFERS_CACHE_KEY_PREFIX + userId;
        String appliedOffersCountCacheKey = APPLIED_OFFERS_COUNT_CACHE_KEY_PREFIX + userId;

        redisTemplate.delete(appliedOffersCacheKey);
        redisTemplate.delete(appliedOffersCountCacheKey);

        return userOfferStatusMapper.toDTO(userOfferStatus);
    }

    public List<OfferDTO> getNotAppliedOffersForUser(String userId) {
        List<Offer> unappliedOffers = queryHandler.getNotAppliedOffersForUser(userId);
        return unappliedOffers.stream()
                .map(offerMapper::toDTO)
                .toList();
    }

    public List<OfferDTO> getAppliedOffersForUser(String userId) {
        String cacheKey = APPLIED_OFFERS_CACHE_KEY_PREFIX + userId;

        // Pobierz dane z cache
        List<OfferDTO> cachedOffers = (List<OfferDTO>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedOffers != null) {
            return cachedOffers;
        }

        // Jeśli brak w cache, pobierz z bazy i zapis w Redis
        List<OfferDTO> appliedOffers = appliedOffersHandler.getAppliedOffersForUser(userId)
                .stream()
                .map(offerMapper::toDTO)
                .toList();

        redisTemplate.opsForValue().set(cacheKey, appliedOffers, 1, TimeUnit.HOURS);
        return appliedOffers;
    }

    public long countAppliedOffersForUser(String userId) {
        String cacheKey = APPLIED_OFFERS_COUNT_CACHE_KEY_PREFIX + userId;

        // Pobierz dane z cache
        Long cachedCount = (Long) redisTemplate.opsForValue().get(cacheKey);
        if (cachedCount != null) {
            return cachedCount;
        }

        // Jeśli brak w cache, pobierz z bazy i zapis w Redis
        long appliedOffersCount = appliedOffersCountHandler.countAppliedOffersForUser(userId);
        redisTemplate.opsForValue().set(cacheKey, appliedOffersCount, 1, TimeUnit.HOURS);
        return appliedOffersCount;
    }
}
