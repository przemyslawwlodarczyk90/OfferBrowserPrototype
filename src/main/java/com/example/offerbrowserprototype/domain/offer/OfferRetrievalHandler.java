package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheFacade;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.service.ExternalJobOfferService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class OfferRetrievalHandler {

    private final OfferRepository offerRepository;
    private final ExternalJobOfferService externalJobOfferService;
    private final OfferCacheFacade offerCacheFacade; // Zmiana z OfferCacheService na OfferCacheFacade
    private final OfferMapper offerMapper;

    public OfferRetrievalHandler(OfferRepository offerRepository,
                                 ExternalJobOfferService externalJobOfferService,
                                 OfferCacheFacade offerCacheFacade, // Zmiana z OfferCacheService na OfferCacheFacade
                                 OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.externalJobOfferService = externalJobOfferService;
        this.offerCacheFacade = offerCacheFacade; // Zmiana z OfferCacheService na OfferCacheFacade
        this.offerMapper = offerMapper;
    }

    public OfferDTO getOffer(String id) {
        return offerRepository.findById(id)
                .map(offerMapper::toDTO) // Użycie mappera do konwersji
                .orElse(null);
    }

    public List<OfferDTO> getAllOffers() {
        // Sprawdź w cache
        List<OfferDTO> cachedOffers = offerCacheFacade.getCachedOffers(); // Użyj offerCacheFacade
        if (cachedOffers != null && !cachedOffers.isEmpty()) {
            return cachedOffers;
        }

        // Pobierz zewnętrzne oferty pracy
        List<OfferDTO> externalOffers = externalJobOfferService.fetchExternalOffers();

        // Zapisz do cache
        offerCacheFacade.cacheOffers(externalOffers); // Użyj offerCacheFacade

        // Zapisz do MongoDB
        externalOffers.forEach(offer -> {
            Offer offerEntity = offerMapper.toEntity(offer); // Konwersja DTO na encję
            offerRepository.save(offerEntity);
        });

        return externalOffers;
    }
}
