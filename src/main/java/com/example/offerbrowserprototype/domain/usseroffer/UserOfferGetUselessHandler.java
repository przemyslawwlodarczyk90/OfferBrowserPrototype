package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.offer.FlagType;
import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.infrastructure.repository.OfferFlagRepository;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserOfferGetUselessHandler {

    private final OfferFlagRepository offerFlagRepository;
    private final OfferRepository offerRepository;

    public UserOfferGetUselessHandler(OfferFlagRepository offerFlagRepository,
                                      OfferRepository offerRepository) {
        this.offerFlagRepository = offerFlagRepository;
        this.offerRepository = offerRepository;
    }

    public List<Offer> getUselessOffersForUser(Long userId) {
        return getUselessOffersForUser(userId, null);
    }

    /** @param source portal to filter by, or {@code null} for all portals */
    public List<Offer> getUselessOffersForUser(Long userId, OfferSource source) {
        List<Long> ids = offerFlagRepository.findOfferIdsByUserIdAndType(userId, FlagType.USELESS);
        if (ids.isEmpty()) return List.of();
        if (source != null) return offerRepository.findByIdInAndSource(ids, source.value());
        return offerRepository.findAllById(ids);
    }
}
