package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.dto.useroffer.UserOfferStatusDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.domain.mapper.UserOfferStatusMapper;
import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.usseroffer.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class UserOfferFacade {

    private final UserOfferApplyHandler applyHandler;
    private final UserOfferMarkUselessHandler markUselessHandler;
    private final UserOfferGetUselessHandler getUselessHandler;
    private final UserOfferStatusMapper statusMapper;
    private final OfferMapper offerMapper;
    private final UserOfferQueryHandler queryHandler;
    private final UserAppliedOffersHandler appliedOffersHandler;
    private final UserAppliedOffersCountHandler countHandler;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String APPLIED_KEY  = "userOffers:applied:";
    private static final String COUNT_KEY    = "userOffers:appliedCount:";
    private static final String USELESS_KEY  = "userOffers:useless:";

    public UserOfferFacade(
            UserOfferApplyHandler applyHandler,
            UserOfferMarkUselessHandler markUselessHandler,
            UserOfferGetUselessHandler getUselessHandler,
            UserOfferStatusMapper statusMapper,
            OfferMapper offerMapper,
            UserOfferQueryHandler queryHandler,
            UserAppliedOffersHandler appliedOffersHandler,
            UserAppliedOffersCountHandler countHandler,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.applyHandler = applyHandler;
        this.markUselessHandler = markUselessHandler;
        this.getUselessHandler = getUselessHandler;
        this.statusMapper = statusMapper;
        this.offerMapper = offerMapper;
        this.queryHandler = queryHandler;
        this.appliedOffersHandler = appliedOffersHandler;
        this.countHandler = countHandler;
        this.redisTemplate = redisTemplate;
    }

    public UserOfferStatusDTO applyToOffer(Long userId, Long offerId) {
        var status = applyHandler.applyToOffer(userId, offerId);
        redisTemplate.delete(APPLIED_KEY + userId);
        redisTemplate.delete(COUNT_KEY + userId);
        return statusMapper.toDTO(status);
    }

    public void markAsUseless(Long userId, Long offerId) {
        markUselessHandler.markAsUseless(userId, offerId);
        redisTemplate.delete(USELESS_KEY + userId);
    }

    public List<OfferDTO> getUselessOffersForUser(Long userId) {
        String key = USELESS_KEY + userId;

        @SuppressWarnings("unchecked")
        List<OfferDTO> cached = (List<OfferDTO>) redisTemplate.opsForValue().get(key);
        if (cached != null) return cached;

        List<OfferDTO> offers = getUselessHandler.getUselessOffersForUser(userId)
                .stream()
                .map(offerMapper::toDTO)
                .toList();

        redisTemplate.opsForValue().set(key, offers, 1, TimeUnit.HOURS);
        return offers;
    }

    public List<OfferDTO> getNotAppliedOffersForUser(Long userId) {
        return queryHandler.getNotAppliedOffersForUser(userId)
                .stream()
                .map(offerMapper::toDTO)
                .toList();
    }

    public List<OfferDTO> getAppliedOffersForUser(Long userId) {
        String key = APPLIED_KEY + userId;

        @SuppressWarnings("unchecked")
        List<OfferDTO> cached = (List<OfferDTO>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        List<OfferDTO> offers = appliedOffersHandler.getAppliedOffersForUser(userId)
                .stream()
                .map(offerMapper::toDTO)
                .toList();

        redisTemplate.opsForValue().set(key, offers, 1, TimeUnit.HOURS);
        return offers;
    }

    public long countAppliedOffersForUser(Long userId) {
        String key = COUNT_KEY + userId;

        Long cached = (Long) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        long count = countHandler.countAppliedOffersForUser(userId);
        redisTemplate.opsForValue().set(key, count, 1, TimeUnit.HOURS);
        return count;
    }
}
