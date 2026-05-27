package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.dto.useroffer.UserOfferStatusDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.domain.mapper.UserOfferStatusMapper;
import com.example.offerbrowserprototype.domain.usseroffer.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public UserOfferFacade(
            UserOfferApplyHandler applyHandler,
            UserOfferMarkUselessHandler markUselessHandler,
            UserOfferGetUselessHandler getUselessHandler,
            UserOfferStatusMapper statusMapper,
            OfferMapper offerMapper,
            UserOfferQueryHandler queryHandler,
            UserAppliedOffersHandler appliedOffersHandler,
            UserAppliedOffersCountHandler countHandler
    ) {
        this.applyHandler = applyHandler;
        this.markUselessHandler = markUselessHandler;
        this.getUselessHandler = getUselessHandler;
        this.statusMapper = statusMapper;
        this.offerMapper = offerMapper;
        this.queryHandler = queryHandler;
        this.appliedOffersHandler = appliedOffersHandler;
        this.countHandler = countHandler;
    }

    public UserOfferStatusDTO applyToOffer(Long userId, Long offerId) {
        return statusMapper.toDTO(applyHandler.applyToOffer(userId, offerId));
    }

    public void markAsUseless(Long userId, Long offerId) {
        markUselessHandler.markAsUseless(userId, offerId);
    }

    @Transactional(readOnly = true)
    public List<OfferDTO> getUselessOffersForUser(Long userId) {
        return getUselessHandler.getUselessOffersForUser(userId)
                .stream()
                .map(offerMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OfferDTO> getNotAppliedOffersForUser(Long userId) {
        return queryHandler.getNotAppliedOffersForUser(userId)
                .stream()
                .map(offerMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OfferDTO> getAppliedOffersForUser(Long userId) {
        return appliedOffersHandler.getAppliedOffersForUser(userId)
                .stream()
                .map(offerMapper::toDTO)
                .toList();
    }

    public long countAppliedOffersForUser(Long userId) {
        return countHandler.countAppliedOffersForUser(userId);
    }
}
