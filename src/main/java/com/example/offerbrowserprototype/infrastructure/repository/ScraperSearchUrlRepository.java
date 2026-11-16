package com.example.offerbrowserprototype.infrastructure.repository;

import com.example.offerbrowserprototype.domain.scraperurl.ScraperSearchUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScraperSearchUrlRepository extends JpaRepository<ScraperSearchUrl, Long> {

    List<ScraperSearchUrl> findBySourceOrderByIdAsc(String source);

    long countBySource(String source);

    boolean existsBySourceAndUrl(String source, String url);

    boolean existsBySourceAndUrlAndIdNot(String source, String url, Long id);
}
