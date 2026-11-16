package com.example.offerbrowserprototype.domain.scraperurl;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * One search (listing) URL a portal's scraper walks through, e.g.
 * {@code https://justjoin.it/job-offers/warszawa/java?experience-levels=junior,mid}.
 * Managed by the admin per portal; passed to the Python script on every run.
 */
@Entity
@Table(name = "scraper_search_urls",
        uniqueConstraints = @UniqueConstraint(name = "uk_scraper_search_url_source_url", columnNames = {"source", "url"}),
        indexes = @Index(name = "idx_scraper_search_url_source", columnList = "source"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScraperSearchUrl {

    public static final int MAX_URL_LENGTH = 1000;
    public static final int MAX_LABEL_LENGTH = 120;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** {@link com.example.offerbrowserprototype.domain.offer.OfferSource#value()} */
    @Column(name = "source", nullable = false, length = 40)
    private String source;

    @Column(name = "url", nullable = false, length = MAX_URL_LENGTH)
    private String url;

    /** Optional description shown in the admin panel, e.g. "Java – Łódź, junior/mid". */
    @Column(name = "label", length = MAX_LABEL_LENGTH)
    private String label;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Username of the admin who added it; {@code null} for the built-in defaults. */
    @Column(name = "created_by", length = 100)
    private String createdBy;
}
