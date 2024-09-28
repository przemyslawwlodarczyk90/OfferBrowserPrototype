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
    private final OfferMapper offerMapper;

    public OfferRetrievalHandler(OfferRepository offerRepository,
                                 ExternalJobOfferService externalJobOfferService,
                                 OfferCacheService offerCacheService,
                                 OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.externalJobOfferService = externalJobOfferService;
        this.offerCacheService = offerCacheService;
        this.offerMapper = offerMapper;
    }

    public OfferDTO getOffer(Long id) {
        return offerRepository.findById(id.toString())
                .map(offerMapper::toDTO) // Użycie mappera do konwersji
                .orElse(null);
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
            Offer offerEntity = offerMapper.toEntity(offer); // Konwersja DTO na encję
            offerRepository.save(offerEntity);
        });

        return externalOffers;
    }
}
