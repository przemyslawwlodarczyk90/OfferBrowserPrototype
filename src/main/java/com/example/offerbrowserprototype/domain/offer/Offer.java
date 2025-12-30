package com.example.offerbrowserprototype.domain.offer;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "job_offers",
        uniqueConstraints = @UniqueConstraint(name = "uk_job_offers_offer_url", columnNames = "offer_url"),
        indexes = {
                @Index(name = "idx_job_offers_fetched_at", columnList = "fetched_at"),
                @Index(name = "idx_job_offers_duplicate", columnList = "is_duplicate")
        })
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "salary_range")
    private String salaryRange;

    @Column(name = "level")
    private String level;

    @Column(name = "offer_url", nullable = false)
    private String offerUrl;

    @Column(name = "is_duplicate", nullable = false)
    private boolean isDuplicate;

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
        this.salaryRange = salaryRange;
        this.level = level;
        this.company = company;
        this.isDuplicate = false;
        this.fetchedAt = LocalDateTime.now(clock);
    }
}
