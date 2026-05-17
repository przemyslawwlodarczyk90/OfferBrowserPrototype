package com.example.offerbrowserprototype.infrastructure.facade;


import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.*;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheFacade;
import com.example.offerbrowserprototype.infrastructure.service.ExternalJobOfferService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OfferFacade {

    private final OfferFromUrlHandler    offerFromUrlHandler;
    private final OfferDetailsHandler    detailsHandler;
    private final OfferAdditionHandler   additionHandler;
    private final OfferUpdateHandler     updateHandler;
    private final OfferDeletionHandler   deletionHandler;
    private final OfferRetrievalHandler  retrievalHandler;
    private final OfferCacheFacade       offerCacheFacade;
    private final ExternalJobOfferService externalJobOfferService;
    private final OfferPushHandler       pushHandler;
    private final MarkAsDuplicateHandler markAsDuplicateHandler;

    public OfferFacade(
            OfferFromUrlHandler    offerFromUrlHandler,
            OfferAdditionHandler   additionHandler,
            OfferUpdateHandler     updateHandler,
            OfferDeletionHandler   deletionHandler,
            OfferRetrievalHandler  retrievalHandler,
            OfferDetailsHandler    detailsHandler,
            OfferCacheFacade       offerCacheFacade,
            ExternalJobOfferService externalJobOfferService,
            OfferPushHandler       pushHandler,
            MarkAsDuplicateHandler markAsDuplicateHandler
    ) {
        this.offerFromUrlHandler   = offerFromUrlHandler;
        this.additionHandler       = additionHandler;
        this.updateHandler         = updateHandler;
        this.deletionHandler       = deletionHandler;
        this.retrievalHandler      = retrievalHandler;
        this.detailsHandler        = detailsHandler;
        this.offerCacheFacade      = offerCacheFacade;
        this.externalJobOfferService = externalJobOfferService;
        this.pushHandler           = pushHandler;
        this.markAsDuplicateHandler = markAsDuplicateHandler;
    }

    @CacheEvict(value = "offers", allEntries = true)
    public OfferDTO addOffer(OfferDTO dto) {
        return additionHandler.addOffer(dto);
    }

    @CacheEvict(value = "offers", allEntries = true)
    public OfferDTO updateOffer(Long id, OfferDTO dto) {
        return updateHandler.updateOffer(id, dto);
    }

    public OfferDTO getOffer(Long id) {
        return detailsHandler.getOfferById(id);
    }

    @CacheEvict(value = "offers", allEntries = true)
    public void deleteOffer(Long id) {
        deletionHandler.deleteOffer(id);
    }

    // ── POPRAWKA: zawsze pobiera z DB, Redis tylko jako opcjonalny cache ──
    public List<OfferDTO> getAllOffers() {

        // 1. Spróbuj Redis (szybka ścieżka)
        try {
            List<OfferDTO> cached = offerCacheFacade.getCachedOffers();
            if (cached != null && !cached.isEmpty()) {
                return cached;
            }
        } catch (Exception ignored) {
            // Redis niedostępny — kontynuuj
        }

        // 2. Zawsze pobierz dane z bazy PostgreSQL
        List<OfferDTO> dbOffers = retrievalHandler.getAllOffers();

        // 3. Dołącz zewnętrznych providerów (cicho pomijaj błędy)
        List<OfferDTO> combined = new ArrayList<>(dbOffers);
        try {
            List<OfferDTO> external = externalJobOfferService.fetchExternalOffers();
            if (external != null && !external.isEmpty()) {
                combined.addAll(external);
            }
        } catch (Exception ignored) {
            // Provider niedostępny — zwróć same dane z DB
        }

        // 4. Zapisz do cache TYLKO jeśli lista nie jest pusta
        if (!combined.isEmpty()) {
            try {
                offerCacheFacade.cacheOffers(combined);
            } catch (Exception ignored) {
                // Redis niedostępny — nie blokuj odpowiedzi
            }
        }

        return combined;
    }

    public void pushOfferToProvider(Long offerId, String provider) {
        pushHandler.pushOfferToProvider(offerId, provider);
    }

    public OfferDTO addOfferFromUrl(Long userId, String offerUrl) {
        return offerFromUrlHandler.addOfferFromUrl(userId, offerUrl);
    }

    @CacheEvict(value = "offers", allEntries = true)
    public void markAsDuplicateById(Long offerId) {
        markAsDuplicateHandler.handleById(offerId);
    }

    @CacheEvict(value = "offers", allEntries = true)
    public void markAsDuplicateByUrl(String offerUrl) {
        markAsDuplicateHandler.handleByUrl(offerUrl);
    }
}