package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    public OfferFromUrlHandler(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public OfferDTO handleOfferFromUrl(String offerUrl) {
        logger.info("Handling offer URL: {}", offerUrl);

        if (offerUrl == null || offerUrl.isBlank()) {
            throw new IllegalArgumentException("Offer URL cannot be null or empty");
        }

        // Check for duplicates
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

            ObjectMapper mapper = new ObjectMapper();
            try {
                OfferDTO offerDTO = mapper.readValue(jsonResponse, OfferDTO.class);
                logger.info("Successfully parsed offer DTO: {}", offerDTO);
                return offerDTO;
            } catch (JsonParseException e) {
                logger.error("Failed to parse JSON: {}. JSON Response: {}", e.getMessage(), jsonResponse);
                throw new RuntimeException("Error parsing JSON from script: " + e.getMessage(), e);
            }

        } catch (Exception e) {
            logger.error("Error executing Python script: {}", e.getMessage(), e);
            throw new RuntimeException("Error scraping offer details: " + e.getMessage(), e);
        }
    }
}
