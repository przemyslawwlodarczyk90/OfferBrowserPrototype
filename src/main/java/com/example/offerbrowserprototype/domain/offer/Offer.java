package com.example.offerbrowserprototype.domain.offer;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.Clock;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "job_offers",
        uniqueConstraints = @UniqueConstraint(name = "uk_job_offers_offer_url", columnNames = "offer_url"),
        indexes = {
                @Index(name = "idx_job_offers_fetched_at", columnList = "fetched_at")
        })
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "location")
    private String location;

    // ⬇️ NOWE POLE - WYMAGANE przez CityDistributionHandler
    @Column(name = "city")
    private String city;

    @Column(name = "salary_range")
    private String salaryRange;

    @Column(name = "level")
    private String level;

    @Column(name = "offer_url", nullable = false, unique = true)
    private String offerUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
            timezone = "UTC")
    @Column(name = "fetched_at")
    private LocalDateTime fetchedAt;

    @Column(name = "company", nullable = false)
    private String company;

    public Offer(String title, String description, String location, String salaryRange,
                 String level, String company, Clock clock) {
        if (company == null || company.trim().isEmpty()) {
            throw new IllegalArgumentException("Company cannot be null or empty.");
        }
        this.title = title;
        this.description = description;
        this.location = location;
        this.city = extractCityFromLocation(location);
        this.salaryRange = salaryRange;
        this.level = level;
        this.company = company;
        this.fetchedAt = LocalDateTime.now(clock);
    }

    private String extractCityFromLocation(String location) {
        if (location == null || location.isBlank()) {
            return "Unknown";
        }
        // np. "Warsaw, Mazovia" → "Warsaw"
        return location.split(",")[0].trim();
    }

    public void setLocation(String location) {
        this.location = location;
        this.city = extractCityFromLocation(location);
    }
}