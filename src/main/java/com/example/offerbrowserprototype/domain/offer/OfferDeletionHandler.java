package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OfferDeletionHandler {

    private final OfferRepository offerRepository;
    private final UserOfferStatusRepository userOfferStatusRepository;

    public OfferDeletionHandler(OfferRepository offerRepository,
                                UserOfferStatusRepository userOfferStatusRepository) {
        this.offerRepository = offerRepository;
        this.userOfferStatusRepository = userOfferStatusRepository;
    }

    @Transactional
    public void deleteOffer(Long id) {
        userOfferStatusRepository.deleteByOffer_Id(id);
        offerRepository.deleteById(id);
    }
}
