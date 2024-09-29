package com.example.offerbrowserprototype.domain.offer;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "job_offers")
public class Offer {

    @Id
    private String id; // MongoDB automatycznie generuje unikalne ID

    private String title;
    private String description;
    private String location;
    private String salaryRange;
    private String technologies;
    private boolean applied; // Dodanie pola 'applied' do śledzenia, czy aplikowałeś na ofertę
    private LocalDateTime fetchedAt; // Data pobrania oferty

    public Offer(String title, String description, String location, String salaryRange, String technologies) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.salaryRange = salaryRange;
        this.technologies = technologies;
        this.applied = false; // Domyślnie oferta nie jest aplikowana
        this.fetchedAt = LocalDateTime.now(); // Ustawienie czasu pobrania
    }
}