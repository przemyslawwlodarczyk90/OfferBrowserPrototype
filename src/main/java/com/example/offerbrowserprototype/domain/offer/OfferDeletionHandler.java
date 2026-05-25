package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.infrastructure.repository.OfferFlagRepository;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OfferDeletionHandler {

    private final OfferRepository offerRepository;
    private final UserOfferStatusRepository userOfferStatusRepository;
    private final OfferFlagRepository offerFlagRepository;

    public OfferDeletionHandler(OfferRepository offerRepository,
                                UserOfferStatusRepository userOfferStatusRepository,
                                OfferFlagRepository offerFlagRepository) {
        this.offerRepository = offerRepository;
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.offerFlagRepository = offerFlagRepository;
    }

    @Transactional
    public void deleteOffer(Long id) {
        userOfferStatusRepository.deleteByOffer_Id(id);
        offerFlagRepository.deleteByOffer_Id(id);
        offerRepository.deleteById(id);
    }
}
