package com.example.offerbrowserprototype.infrastructure.service;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.repository.OfferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class OfferImportService {

    private static final Logger logger = LoggerFactory.getLogger(OfferImportService.class);

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void importOffersFromJson(String filePath) {
        try {

            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
            }

            File file = new File(filePath);

            if (!file.exists()) {
                logger.error("Plik nie istnieje: {}", filePath);
                throw new IOException("Plik nie został znaleziony: " + filePath);
            }

            // Wczytanie ofert z pliku JSON
            List<Offer> offers = List.of(objectMapper.readValue(file, Offer[].class));
            logger.info("Liczba wczytanych ofert z pliku: {}", offers.size());

            for (Offer offer : offers) {
                try {
                    if (offerRepository.findByOfferUrl(offer.getOfferUrl()).isEmpty()) {
                        offerRepository.save(offer);
                        logger.info("Zapisano nową ofertę: {}", offer.getTitle());
                    } else {
                        logger.info("Oferta już istnieje w bazie: {}", offer.getOfferUrl());
                    }
                } catch (DateTimeParseException e) {
                    logger.error("Niepoprawny format daty w polu fetchedAt dla oferty {}: {}", offer.getOfferUrl(), e.getMessage());
                } catch (Exception e) {
                    logger.error("Błąd przy przetwarzaniu oferty {}: {}", offer.getOfferUrl(), e.getMessage());
                }
            }

            logger.info("Import ofert zakończony sukcesem.");
        } catch (IOException e) {
            logger.error("Błąd podczas odczytu pliku JSON: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Nieoczekiwany błąd podczas importu ofert: {}", e.getMessage());
        }
    }
}
