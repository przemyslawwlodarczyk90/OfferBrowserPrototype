package com.example.offerbrowserprototype.domain.offer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Clock;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "job_offers")
public class Offer {

    @Id
    private String id; // MongoDB automatycznie generuje unikalne ID

    private String title; // Tytuł oferty
    private String description; // Opis oferty
    private String location; // Lokalizacja oferty
    private String salaryRange; // Zakres wynagrodzenia
    private String level; // Poziom doświadczenia

    @Indexed(unique = true)
    private String offerUrl; // URL oferty (unikalny)

    private boolean applied; // Czy użytkownik aplikował na ofertę

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC")
    private LocalDateTime fetchedAt; // Data pobrania oferty

    /**
     * Konstruktor pozwalający na inicjalizację pola fetchedAt przy użyciu Clock.
     */
    public Offer(String title, String description, String location, String salaryRange, String level, Clock clock) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.salaryRange = salaryRange;
        this.level = level;
        this.applied = false; // Domyślnie oferta nie została zaaplikowana
        this.fetchedAt = LocalDateTime.now(clock); // Ustawienie bieżącego czasu przy użyciu Clock
    }
}
