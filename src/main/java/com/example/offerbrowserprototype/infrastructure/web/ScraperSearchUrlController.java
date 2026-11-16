package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.scraperurl.SaveScraperSearchUrlRequest;
import com.example.offerbrowserprototype.domain.dto.scraperurl.ScraperSearchUrlDTO;
import com.example.offerbrowserprototype.domain.offer.OfferSource;
import com.example.offerbrowserprototype.domain.scraperurl.DefaultSearchUrls;
import com.example.offerbrowserprototype.infrastructure.facade.ScraperSearchUrlFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/scraper-urls")
@Tag(name = "Admin – scraper search URLs",
     description = "Search (listing) URLs each portal's scraper walks through — one list per portal, edited by the admin.")
@PreAuthorize("hasRole('ADMIN')")
public class ScraperSearchUrlController {

    private final ScraperSearchUrlFacade facade;

    public ScraperSearchUrlController(ScraperSearchUrlFacade facade) {
        this.facade = facade;
    }

    @GetMapping
    @Operation(summary = "Search URLs of a portal", description = "In the order they were added — the scraper visits them in this order.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned"),
            @ApiResponse(responseCode = "400", description = "Unknown source"),
            @ApiResponse(responseCode = "403", description = "Admin only")
    })
    public List<ScraperSearchUrlDTO> list(
            @Parameter(description = "NoFluff, TheProtocol or JustJoin") @RequestParam String source) {
        return facade.list(parse(source));
    }

    @GetMapping("/defaults")
    @Operation(summary = "Built-in search URLs of a portal")
    public List<DefaultSearchUrls.Entry> defaults(@RequestParam String source) {
        return facade.defaults(parse(source));
    }

    @PostMapping
    @Operation(summary = "Add a search URL", description = "The URL must be on the portal's domain; duplicates are rejected (409). "
            + "Takes effect on the next scraper run.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "URL added"),
            @ApiResponse(responseCode = "400", description = "Invalid URL or foreign domain"),
            @ApiResponse(responseCode = "409", description = "URL already on the list")
    })
    public ResponseEntity<ScraperSearchUrlDTO> add(@RequestParam String source,
                                                   @RequestBody SaveScraperSearchUrlRequest body,
                                                   @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facade.add(parse(source), body.url(), body.label(), username(principal)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace a search URL (e.g. new search parameters) and/or its description")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "URL updated"),
            @ApiResponse(responseCode = "400", description = "Invalid URL or foreign domain"),
            @ApiResponse(responseCode = "404", description = "No such URL"),
            @ApiResponse(responseCode = "409", description = "URL already on the list")
    })
    public ScraperSearchUrlDTO update(@PathVariable Long id, @RequestBody SaveScraperSearchUrlRequest body,
                                      @AuthenticationPrincipal UserDetails principal) {
        return facade.update(id, body.url(), body.label(), username(principal));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a search URL", description = "The last URL of a portal cannot be removed (409) — add its replacement first.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "URL removed"),
            @ApiResponse(responseCode = "404", description = "No such URL"),
            @ApiResponse(responseCode = "409", description = "Last URL of the portal")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {
        facade.delete(id, username(principal));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/restore-defaults")
    @Operation(summary = "Add the missing built-in URLs of a portal", description = "Existing entries are kept.")
    public Map<String, Object> restoreDefaults(@RequestParam String source,
                                               @AuthenticationPrincipal UserDetails principal) {
        OfferSource portal = parse(source);
        int added = facade.restoreDefaults(portal, username(principal));
        return Map.of("added", added, "urls", facade.list(portal));
    }

    private static String username(UserDetails principal) {
        return principal == null ? null : principal.getUsername();
    }

    private static OfferSource parse(String source) {
        return OfferSource.fromParam(source)
                .orElseThrow(() -> new IllegalArgumentException("Podaj źródło: " + OfferSource.supportedValues()));
    }
}
