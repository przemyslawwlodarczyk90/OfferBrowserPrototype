package com.example.offerbrowserprototype.infrastructure.service;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.requirement.NiceToHaveCreationHandler;
import com.example.offerbrowserprototype.domain.requirement.RequirementCreationHandler;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class OfferImportService {

    private static final Logger logger = LoggerFactory.getLogger(OfferImportService.class);
    private final OfferRepository offerRepository;
    private final ObjectMapper objectMapper;
    private final RequirementCreationHandler requirementCreationHandler;
    private final NiceToHaveCreationHandler niceToHaveCreationHandler;

    public OfferImportService(
            OfferRepository offerRepository,
            ObjectMapper objectMapper,
            RequirementCreationHandler requirementCreationHandler,
            NiceToHaveCreationHandler niceToHaveCreationHandler
    ) {
        this.offerRepository = offerRepository;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.requirementCreationHandler = requirementCreationHandler;
        this.niceToHaveCreationHandler = niceToHaveCreationHandler;
    }

    public void importOffersFromJson(String filePath) {
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                logger.error("File does not exist: {}", filePath);
                throw new IOException("File not found: " + filePath);
            }

            List<Offer> offers = List.of(objectMapper.readValue(file, Offer[].class));
            logger.info("Number of offers read from file: {}", offers.size());

            for (Offer offer : offers) {
                try {
                    if (offer.getTitle() != null) {
                        offer.setTitle(offer.getTitle().replaceAll("(?i)\\s*NOWA\\s*$", "").trim());
                    }
                    java.util.Optional<Offer> existingOpt = offerRepository.findByOfferUrl(offer.getOfferUrl());
                    if (existingOpt.isEmpty()) {
                        Offer saved = offerRepository.save(offer);
                        requirementCreationHandler.createFromOffer(saved);
                        niceToHaveCreationHandler.createFromOffer(saved);
                        logger.info("Saved new offer: {}", offer.getTitle());
                    } else {
                        Offer existing = existingOpt.get();
                        boolean updated = false;
                        if ((existing.getRequirements() == null || existing.getRequirements().isEmpty())
                                && offer.getRequirements() != null && !offer.getRequirements().isEmpty()) {
                            existing.setRequirements(offer.getRequirements());
                            updated = true;
                        }
                        if ((existing.getNiceToHave() == null || existing.getNiceToHave().isEmpty())
                                && offer.getNiceToHave() != null && !offer.getNiceToHave().isEmpty()) {
                            existing.setNiceToHave(offer.getNiceToHave());
                            updated = true;
                        }
                        if (existing.getSource() == null && offer.getSource() != null) {
                            existing.setSource(offer.getSource());
                            updated = true;
                        }
                        if (updated) {
                            Offer saved = offerRepository.save(existing);
                            requirementCreationHandler.createFromOffer(saved);
                            niceToHaveCreationHandler.createFromOffer(saved);
                            logger.info("Backfilled requirements for existing offer: {}", offer.getTitle());
                        } else {
                            logger.info("Offer already exists, no new data to backfill: {}", offer.getOfferUrl());
                        }
                    }
                } catch (DateTimeParseException e) {
                    logger.error("Invalid date format in field fetchedAt for offer {}: {}", offer.getOfferUrl(), e.getMessage());
                } catch (Exception e) {
                    logger.error("Error processing offer {}: {}", offer.getOfferUrl(), e.getMessage());
                }
            }

            logger.info("Offer import completed successfully.");
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during offer import: {}", e.getMessage());
        }
    }
}
