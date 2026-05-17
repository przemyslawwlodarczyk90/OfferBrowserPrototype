package com.example.offerbrowserprototype.infrastructure.service;

import com.example.offerbrowserprototype.domain.offer.Offer;
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

    public OfferImportService(OfferRepository offerRepository, ObjectMapper objectMapper) {
        this.offerRepository = offerRepository;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
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
                    if (offerRepository.findByOfferUrl(offer.getOfferUrl()).isEmpty()) {
                        offerRepository.save(offer);
                        logger.info("Saved new offer: {}", offer.getTitle());
                    } else {
                        logger.info("Offer already exists in database: {}", offer.getOfferUrl());
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
