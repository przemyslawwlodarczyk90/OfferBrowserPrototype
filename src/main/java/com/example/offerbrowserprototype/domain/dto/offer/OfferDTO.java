package com.example.offerbrowserprototype.domain.dto.offer;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OfferDTO {
    private String id;
    private String title;
    private String description;
    private String location;
    private String salaryRange;
    private String technologies;
    private boolean applied;
    private LocalDateTime fetchedAt;

    // Konstruktor bez ID (dla tworzenia nowych ofert)
    public OfferDTO(String title, String description, String location, String salaryRange, String technologies, boolean applied, LocalDateTime fetchedAt) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.salaryRange = salaryRange;
        this.technologies = technologies;
        this.applied = applied; // Ustawienie pola 'applied'
        this.fetchedAt = fetchedAt; // Ustawienie pola 'fetchedAt'
    }

    // Konstruktor z ID (dla istniejących ofert)
    public OfferDTO(String id, String title, String description, String location, String salaryRange, String technologies, boolean applied, LocalDateTime fetchedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.salaryRange = salaryRange;
        this.technologies = technologies;
        this.applied = applied; // Ustawienie pola 'applied'
        this.fetchedAt = fetchedAt; // Ustawienie pola 'fetchedAt'
    }

    public OfferDTO() {
    }


}
