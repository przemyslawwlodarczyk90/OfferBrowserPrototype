package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferApplyHandler;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferQueryHandler;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferStatusCreationHandler;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserOfferFacade {

    private final UserOfferApplyHandler applyHandler;
    private final UserOfferQueryHandler queryHandler;
    private final UserOfferStatusCreationHandler statusCreationHandler;
    private final OfferMapper offerMapper;

    public UserOfferFacade(
            UserOfferApplyHandler applyHandler,
            UserOfferQueryHandler queryHandler,
            UserOfferStatusCreationHandler statusCreationHandler,
            OfferMapper offerMapper) {
        this.applyHandler = applyHandler;
        this.queryHandler = queryHandler;
        this.statusCreationHandler = statusCreationHandler;
        this.offerMapper = offerMapper;
    }

    public void applyToOffer(String userId, String offerId) {
        statusCreationHandler.createStatusIfNotExists(userId, offerId);
        applyHandler.applyToOffer(userId, offerId);
    }

    public List<OfferDTO> getNotAppliedOffersForUser(String userId) {
        // Wywołanie handlera i mapowanie na DTO
        return queryHandler.getNotAppliedOffersForUser(userId)
                .stream()
                .map(offerMapper::toDTO)
                .toList();
    }
//
//    public List<OfferDTO> getAppliedOffersForUser(String userId) {
//        return queryHandler.getAppliedOffersForUser(userId)
//                .stream()
//                .map(offerMapper::toDTO)
//                .toList();
//    }
}
