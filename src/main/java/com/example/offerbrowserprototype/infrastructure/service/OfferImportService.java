package com.example.offerbrowserprototype.infrastructure.service;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.domain.offer.OfferValidation;
import com.example.offerbrowserprototype.domain.requirement.NiceToHaveCreationHandler;
import com.example.offerbrowserprototype.domain.requirement.RequirementCreationHandler;
import com.example.offerbrowserprototype.infrastructure.cache.OfferCacheInvalidator;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Imports scraped offers. Each portal imports only its own file and only offers
 * that belong to it; existing rows are matched by offerUrl, so re-imports never
 * duplicate offers, never delete anything and never touch users' statuses.
 */
@Service
public class OfferImportService {

    private static final Logger logger = LoggerFactory.getLogger(OfferImportService.class);
    private final OfferRepository offerRepository;
    private final ObjectMapper objectMapper;
    private final RequirementCreationHandler requirementCreationHandler;
    private final NiceToHaveCreationHandler niceToHaveCreationHandler;
    private final OfferCacheInvalidator cacheInvalidator;
    private final TransactionTemplate perOfferTransaction;

    private enum Outcome { IMPORTED, UPDATED, SKIPPED }

    public record ImportResult(String source, int read, int imported, int updated, int skipped, int rejected, int errors) {

        public Map<String, Object> toMap() {
            return Map.of("source", source, "total", read, "imported", imported, "updated", updated,
                    "skipped", skipped, "rejected", rejected, "errors", errors);
        }
    }

    public OfferImportService(
            OfferRepository offerRepository,
            ObjectMapper objectMapper,
            RequirementCreationHandler requirementCreationHandler,
            NiceToHaveCreationHandler niceToHaveCreationHandler,
            OfferCacheInvalidator cacheInvalidator,
            PlatformTransactionManager transactionManager
    ) {
        this.offerRepository = offerRepository;
        this.objectMapper = objectMapper.copy()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.requirementCreationHandler = requirementCreationHandler;
        this.niceToHaveCreationHandler = niceToHaveCreationHandler;
        this.cacheInvalidator = cacheInvalidator;
        // one transaction per offer: lazy collections of existing rows are readable,
        // and one broken offer never rolls back the others
        this.perOfferTransaction = new TransactionTemplate(transactionManager);
    }

    /** Legacy entry point — the No Fluff Jobs file. */
    public void importOffersFromJson(String filePath) {
        try {
            importOffersFromJson(filePath, OfferSource.NOFLUFF);
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", e.getMessage());
        }
    }

    public ImportResult importOffersFromJson(String filePath, OfferSource expectedSource) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Plik nie istnieje: " + filePath);
        }
        List<Offer> offers = List.of(objectMapper.readValue(file, Offer[].class));
        logger.info("[{}] Number of offers read from {}: {}", expectedSource.value(), filePath, offers.size());
        return importOffers(offers, expectedSource);
    }

    /** Import offers posted as JSON (Import page). */
    public ImportResult importFromPayload(List<Map<String, Object>> payload, OfferSource expectedSource) {
        List<Offer> offers = payload.stream().map(this::toOffer).toList();
        return importOffers(offers, expectedSource);
    }

    private Offer toOffer(Map<String, Object> item) {
        Offer offer;
        try {
            offer = objectMapper.convertValue(item, Offer.class);
        } catch (IllegalArgumentException e) {
            // most often a hand-made file with a differently formatted fetchedAt — import without it
            Map<String, Object> withoutDate = new java.util.HashMap<>(item);
            withoutDate.remove("fetchedAt");
            withoutDate.remove("id");
            try {
                offer = objectMapper.convertValue(withoutDate, Offer.class);
            } catch (IllegalArgumentException e2) {
                logger.warn("Invalid offer object skipped: {}", e2.getMessage());
                return new Offer();
            }
        }
        offer.setId(null);
        if (offer.getFetchedAt() == null) {
            offer.setFetchedAt(LocalDateTime.now());
        }
        return offer;
    }

    ImportResult importOffers(List<Offer> offers, OfferSource expectedSource) {
        int imported = 0, updated = 0, skipped = 0, rejected = 0, errors = 0;

        for (Offer offer : offers) {
            try {
                String rejection = validate(offer, expectedSource);
                if (rejection != null) {
                    rejected++;
                    logger.warn("[{}] Rejected offer {}: {}", expectedSource.value(), offer.getOfferUrl(), rejection);
                    continue;
                }
                offer.setSource(expectedSource.value());
                if (offer.getTitle() != null) {
                    offer.setTitle(offer.getTitle().replaceAll("(?i)\\s*NOWA\\s*$", "").trim());
                }
                Outcome outcome = perOfferTransaction.execute(tx -> saveOrBackfill(offer));
                switch (outcome) {
                    case IMPORTED -> imported++;
                    case UPDATED -> updated++;
                    default -> skipped++;
                }
            } catch (Exception e) {
                errors++;
                logger.error("Error processing offer {}: {}", offer.getOfferUrl(), e.getMessage());
            }
        }

        ImportResult result = new ImportResult(expectedSource.value(), offers.size(), imported, updated, skipped, rejected, errors);
        logger.info("[{}] Offer import finished: {}", expectedSource.value(), result);
        if (imported + updated > 0) {
            cacheInvalidator.invalidateAll();
        }
        return result;
    }

    private Outcome saveOrBackfill(Offer offer) {
        Optional<Offer> existingOpt = offerRepository.findByOfferUrl(offer.getOfferUrl());
        if (existingOpt.isEmpty()) {
            Offer saved = offerRepository.save(offer);
            requirementCreationHandler.createFromOffer(saved);
            niceToHaveCreationHandler.createFromOffer(saved);
            logger.info("Saved new offer: {}", offer.getTitle());
            return Outcome.IMPORTED;
        }
        return backfill(existingOpt.get(), offer) ? Outcome.UPDATED : Outcome.SKIPPED;
    }

    private static String validate(Offer offer, OfferSource expectedSource) {
        if (offer.getSource() != null && !offer.getSource().isBlank()
                && !offer.getSource().equalsIgnoreCase(expectedSource.value())) {
            return "oferta z innego źródła (" + offer.getSource() + ")";
        }
        return OfferValidation.rejectionReason(offer.getOfferUrl(), offer.getTitle(), offer.getCompany(), expectedSource);
    }

    /** Fills data missing on an existing row; never overwrites what the user already has. */
    private boolean backfill(Offer existing, Offer incoming) {
        boolean updated = false;
        if ((existing.getRequirements() == null || existing.getRequirements().isEmpty())
                && incoming.getRequirements() != null && !incoming.getRequirements().isEmpty()) {
            existing.setRequirements(incoming.getRequirements());
            updated = true;
        }
        if ((existing.getNiceToHave() == null || existing.getNiceToHave().isEmpty())
                && incoming.getNiceToHave() != null && !incoming.getNiceToHave().isEmpty()) {
            existing.setNiceToHave(incoming.getNiceToHave());
            updated = true;
        }
        if (existing.getSource() == null && incoming.getSource() != null) {
            existing.setSource(incoming.getSource());
            updated = true;
        }
        if (updated) {
            Offer saved = offerRepository.save(existing);
            requirementCreationHandler.createFromOffer(saved);
            niceToHaveCreationHandler.createFromOffer(saved);
            logger.info("Backfilled data for existing offer: {}", existing.getOfferUrl());
        }
        return updated;
    }
}
