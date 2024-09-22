package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class OfferRetrievalHandler {

    private final OfferRepository offerRepository;
    private final ExternalJobOfferService externalJobOfferService;
    private final OfferCacheService offerCacheService;

    public OfferRetrievalHandler(OfferRepository offerRepository, ExternalJobOfferService externalJobOfferService, OfferCacheService offerCacheService) {
        this.offerRepository = offerRepository;
        this.externalJobOfferService = externalJobOfferService;
        this.offerCacheService = offerCacheService;
    }

    public OfferDTO getOffer(Long id) {
        return offerRepository.findById(id.toString()).orElse(null);
    }

    public List<OfferDTO> getAllOffers() {
        // Sprawdź w cache
        List<OfferDTO> cachedOffers = offerCacheService.getCachedOffers();
        if (cachedOffers != null && !cachedOffers.isEmpty()) {
            return cachedOffers;
        }

        // Pobierz zewnętrzne oferty pracy
        List<OfferDTO> externalOffers = externalJobOfferService.fetchExternalOffers();

        // Zapisz do cache
        offerCacheService.cacheOffers(externalOffers);

        // Zapisz do MongoDB
        externalOffers.forEach(offer -> {
            Offer offerEntity = new Offer();
            offerEntity.setId(offer.getId().toString());
            offerEntity.setTitle(offer.getTitle());
            offerEntity.setDescription(offer.getDescription());
            offerEntity.setLocation(offer.getLocation());
            offerEntity.setSalaryRange(offer.getSalaryRange());
            offerEntity.setTechnologies(offer.getTechnologies());
            offerRepository.save(offerEntity);
        });

        return externalOffers;
    }
}
