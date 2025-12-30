package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.aplicationnote.ApplicationNoteHandler;
import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferStatus;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
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
    private String pythonPath;

    @Value("${python.script.offer-url-scraper}")
    private String scriptPath;

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final ApplicationNoteHandler applicationNoteHandler;
    private final UserOfferStatusRepository userOfferStatusRepository;

    public OfferFromUrlHandler(
            OfferRepository offerRepository,
            OfferMapper offerMapper,
            ApplicationNoteHandler applicationNoteHandler,
            UserOfferStatusRepository userOfferStatusRepository
    ) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.applicationNoteHandler = applicationNoteHandler;
        this.userOfferStatusRepository = userOfferStatusRepository;
    }

    public OfferDTO addOfferFromUrl(String userId, String offerUrl) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        if (offerUrl == null || offerUrl.isBlank()) {
            throw new IllegalArgumentException("Offer URL cannot be null or empty.");
        }

        // Scrape
        OfferDTO offerDto = handleOfferFromUrl(offerUrl);

        // Map + save
        Offer offer = offerMapper.toEntity(offerDto);
        offer = offerRepository.save(offer);

        String offerIdAsString = offer.getId().toString();
        logger.info("Saved offer with ID: {}", offerIdAsString);

        // Create UserOfferStatus
        UserOfferStatus status = new UserOfferStatus(userId, offerIdAsString, false);
        status.setAppliedAt(LocalDateTime.now());
        userOfferStatusRepository.save(status);

        logger.info("Created UserOfferStatus for userId: {}, offerId: {}", userId, offerIdAsString);

        // Create ApplicationNote
        applicationNoteHandler.saveApplicationNote(
                userId,
                offerIdAsString,
                offer.getOfferUrl(),
                offer.getCompany()
        );

        return offerMapper.toDTO(offer);
    }

    public OfferDTO handleOfferFromUrl(String offerUrl) {
        logger.info("Handling offer URL: {}", offerUrl);

        if (offerUrl == null || offerUrl.isBlank()) {
            throw new IllegalArgumentException("Offer URL cannot be null or empty.");
        }

        Optional<Offer> existingOffer = offerRepository.findByOfferUrl(offerUrl);
        if (existingOffer.isPresent()) {
            logger.warn("Offer with URL {} already exists.", offerUrl);
            throw new IllegalStateException("Offer already exists in the database.");
        }

        ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, scriptPath, offerUrl);
        processBuilder.redirectErrorStream(true);

        try {
            logger.info("Starting Python script: {} {} {}", pythonPath, scriptPath, offerUrl);
            Process process = processBuilder.start();

            String output;
            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                output = reader.lines().collect(Collectors.joining("\n"));
            }

            int exitCode = process.waitFor();
            logger.info("Python script finished with exit code: {}", exitCode);

            if (exitCode != 0) {
                throw new RuntimeException("Python script failed. Output: " + output);
            }

            String[] lines = output.split("\n");
            String jsonResponse = lines[lines.length - 1];

            return new ObjectMapper().readValue(jsonResponse, OfferDTO.class);

        } catch (Exception e) {
            logger.error("Error scraping offer from URL", e);
            throw new RuntimeException("Error scraping offer details", e);
        }
    }
}
