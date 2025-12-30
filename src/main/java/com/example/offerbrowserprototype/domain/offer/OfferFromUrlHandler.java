package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNoteHandler;
import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferStatus;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
@Component
public class OfferFromUrlHandler {

    private static final Logger logger = LoggerFactory.getLogger(OfferFromUrlHandler.class);

    @Value("${python.path}")
    public String pythonPath;

    @Value("${python.script.offer-url-scraper}")
    public String scriptPath;

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final ApplicationNoteHandler applicationNoteHandler;
    private final UserOfferStatusRepository userOfferStatusRepository;

    public OfferFromUrlHandler(OfferRepository offerRepository,
                               OfferMapper offerMapper,
                               ApplicationNoteHandler applicationNoteHandler,
                               UserOfferStatusRepository userOfferStatusRepository) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.applicationNoteHandler = applicationNoteHandler;
        this.userOfferStatusRepository = userOfferStatusRepository;
    }

    public OfferDTO addOfferFromUrl(String userId, String offerUrl) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (offerUrl == null || offerUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Offer URL cannot be null or empty.");
        }

        // Pobierz szczegóły oferty
        OfferDTO offerDto = handleOfferFromUrl(offerUrl);
        if (offerDto == null) {
            throw new IllegalArgumentException("Failed to scrape offer details from URL: " + offerUrl);
        }

        // Zapisz ofertę w bazie danych
        Offer offer = offerMapper.toEntity(offerDto);
        offer = offerRepository.save(offer);

        logger.info("Saved offer with ID: {}", offer.getId());

        // Utwórz rekord UserOffer
        UserOfferStatus userOfferStatus = new UserOfferStatus(userId, offer.getId(), false);
        userOfferStatus.setAppliedAt(LocalDateTime.now());
        userOfferStatusRepository.save(userOfferStatus);

        logger.info("Created UserOffer record for userId: {}, offerId: {}", userId, offer.getId());

        // Utwórz notatkę aplikacyjną
        applicationNoteHandler.saveApplicationNote(userId, offer.getId(), offer.getOfferUrl(), offer.getCompany());

        return offerMapper.toDTO(offer);
    }

    public OfferDTO handleOfferFromUrl(String offerUrl) {
        logger.info("Handling offer URL: {}", offerUrl);

        if (offerUrl == null || offerUrl.isBlank()) {
            throw new IllegalArgumentException("Offer URL cannot be null or empty");
        }

        Optional<Offer> existingOffer = offerRepository.findByOfferUrl(offerUrl);
        if (existingOffer.isPresent()) {
            logger.warn("Offer with URL {} already exists in the database.", offerUrl);
            throw new IllegalStateException("Offer already exists in the database");
        }

        ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, scriptPath, offerUrl);
        processBuilder.redirectErrorStream(true);

        try {
            logger.info("Starting Python script: {} {} {}", pythonPath, scriptPath, offerUrl);
            Process process = processBuilder.start();

            String output;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                output = reader.lines().collect(Collectors.joining("\n"));
            }

            int exitCode = process.waitFor();
            logger.info("Python script completed with exit code: {}", exitCode);

            if (exitCode != 0) {
                logger.error("Python script failed with exit code: {}. Output: {}", exitCode, output);
                throw new RuntimeException("Python script failed with exit code " + exitCode);
            }

            logger.info("Python script output: {}", output);

            String[] lines = output.split("\n");
            String jsonResponse = lines[lines.length - 1];

            return new ObjectMapper().readValue(jsonResponse, OfferDTO.class);

        } catch (Exception e) {
            logger.error("Error executing Python script: {}", e.getMessage(), e);
            throw new RuntimeException("Error scraping offer details: " + e.getMessage(), e);
        }
    }
}
