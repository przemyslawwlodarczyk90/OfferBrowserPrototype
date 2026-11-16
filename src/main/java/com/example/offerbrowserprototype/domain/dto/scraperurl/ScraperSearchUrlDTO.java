package com.example.offerbrowserprototype.domain.dto.scraperurl;

import com.example.offerbrowserprototype.domain.scraperurl.ScraperSearchUrl;

import java.time.LocalDateTime;

public record ScraperSearchUrlDTO(
        Long id,
        String source,
        String url,
        String label,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        boolean isDefault
) {
    public static ScraperSearchUrlDTO from(ScraperSearchUrl e, boolean isDefault) {
        return new ScraperSearchUrlDTO(e.getId(), e.getSource(), e.getUrl(), e.getLabel(),
                e.getCreatedAt(), e.getUpdatedAt(), e.getCreatedBy(), isDefault);
    }
}
