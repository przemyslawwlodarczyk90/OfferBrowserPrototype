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
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserOfferFacade {

    private final UserOfferApplyHandler applyHandler;
    private final UserRepository userRepository;
    private final UserOfferStatusMapper userOfferStatusMapper;
    private final OfferMapper offerMapper;
    private final UserOfferQueryHandler queryHandler;

    private final UserAppliedOffersHandler appliedOffersHandler;
    private final UserAppliedOffersCountHandler appliedOffersCountHandler;
    public UserOfferFacade(UserOfferApplyHandler applyHandler,
                           UserRepository userRepository,
                           UserOfferStatusMapper userOfferStatusMapper,
                           OfferMapper offerMapper,
                           UserOfferQueryHandler queryHandler, UserAppliedOffersHandler appliedOffersHandler, UserAppliedOffersCountHandler appliedOffersCountHandler) {
        this.applyHandler = applyHandler;
        this.userRepository = userRepository;
        this.userOfferStatusMapper = userOfferStatusMapper;
        this.offerMapper = offerMapper;
        this.queryHandler = queryHandler;
        this.appliedOffersHandler = appliedOffersHandler;
        this.appliedOffersCountHandler = appliedOffersCountHandler;
    }

    public UserOfferStatusDTO applyToOfferByEmail(String email, String offerId) {
        String userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"))
                .getId();

        var userOfferStatus = applyHandler.applyToOffer(userId, offerId);

        return userOfferStatusMapper.toDTO(userOfferStatus);
    }

    public List<OfferDTO> getNotAppliedOffersForUser(String userId) {

        List<Offer> unappliedOffers = queryHandler.getNotAppliedOffersForUser(userId);
        return unappliedOffers.stream()
                .map(offerMapper::toDTO)
                .toList();
    }

    public List<OfferDTO> getAppliedOffersForUser(String userId) {
        return appliedOffersHandler.getAppliedOffersForUser(userId)
                .stream()
                .map(offerMapper::toDTO)
                .toList();
    }

    public long countAppliedOffersForUser(String userId) {
        return appliedOffersCountHandler.countAppliedOffersForUser(userId);
    }
}
