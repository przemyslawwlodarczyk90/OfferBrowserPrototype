package com.offerbrowserprototype.domain.offer;

import com.offerbrowserprototype.domain.offer.dto.OfferDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Arrays;

@Component
class OfferRetrievalHandler {

    OfferDTO getOffer(Long id) {
        // Zwraca konkretną ofertę na podstawie id; na potrzeby przykładu zwracane są statyczne dane
        return new OfferDTO(id, "Software Developer", "Full-time job for software development", "New York", "100,000 - 120,000 USD", "Java, Spring Boot", "Full-time");
    }

    List<OfferDTO> getAllOffers() {
        // Przykładowe dane dla demonstracji
        return Arrays.asList(
                new OfferDTO(1L, "Software Developer", "Full-time job for software development", "New York", "100,000 - 120,000 USD", "Java, Spring Boot", "Full-time"),
                new OfferDTO(2L, "Data Scientist", "Data Science in FinTech", "San Francisco", "120,000 - 150,000 USD", "Python, Machine Learning", "Contract"),
                new OfferDTO(3L, "Product Manager", "Managing product lifecycle at a startup", "Remote", "80,000 - 100,000 USD", "Agile, Scrum", "Part-time")
        );
    }
}