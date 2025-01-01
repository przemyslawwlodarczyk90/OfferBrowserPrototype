package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class OfferFromUrlHandler {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    public OfferFromUrlHandler(OfferRepository offerRepository, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
    }

    public void handleOfferFromUrl(String offerUrl) {
        OfferDTO offerDTO = scrapeOfferDetails(offerUrl);
        Offer offer = offerMapper.toEntity(offerDTO);
        offerRepository.save(offer);
    }

    private OfferDTO scrapeOfferDetails(String offerUrl) {
        // Wywołanie skryptu Selenium w Pythonie
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "path/to/your_scraper.py", offerUrl);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                StringBuilder output = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }

                process.waitFor();
                String jsonOutput = output.toString();
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(jsonOutput, OfferDTO.class);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error scraping offer details: " + e.getMessage(), e);
        }
    }
}
