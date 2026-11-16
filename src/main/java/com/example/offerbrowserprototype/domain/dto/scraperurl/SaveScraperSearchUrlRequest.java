package com.example.offerbrowserprototype.domain.dto.scraperurl;

/** Body of add/edit: the search URL with its parameters and an optional description. */
public record SaveScraperSearchUrlRequest(String url, String label) {
}
