package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.infrastructure.facade.UserOfferFacade;
import com.example.offerbrowserprototype.infrastructure.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user-offers")
@Tag(name = "User Offers", description = "Manage user-specific offers")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserOfferController {

    private final UserOfferFacade userOfferFacade;

    @PostMapping("/{offerId}/apply")
    @Operation(summary = "Apply to an offer", description = "Marks an offer as applied for the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully applied to the offer"),
            @ApiResponse(responseCode = "404", description = "Offer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @GetMapping("/{offerId}/apply")
    public ResponseEntity<Void> applyToOfferGet(
            @RequestHeader("userId") String userId,
            @PathVariable String offerId) {
        userOfferFacade.applyToOffer(userId, offerId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/not-applied")
    @Operation(summary = "Get not applied offers", description = "Retrieves offers the user hasn't applied to.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved unapplied offers"),
            @ApiResponse(responseCode = "404", description = "No unapplied offers found")
    })
    public ResponseEntity<List<OfferDTO>> getNotAppliedOffers(@RequestHeader("userId") String userId) {
        List<OfferDTO> notAppliedOffers = userOfferFacade.getNotAppliedOffersForUser(userId);
        if (notAppliedOffers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notAppliedOffers);
    }

    @GetMapping("/applied")
    @Operation(summary = "Get applied offers", description = "Retrieves offers the user has applied to.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved applied offers"),
            @ApiResponse(responseCode = "404", description = "No applied offers found")
    })
    public ResponseEntity<List<OfferDTO>> getAppliedOffers(@RequestHeader("userId") String userId) {
        List<OfferDTO> appliedOffers = userOfferFacade.getAppliedOffersForUser(userId);
        if (appliedOffers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(appliedOffers);
    }

    @GetMapping("/daily-unapplied")
    @Operation(summary = "Get daily unapplied offers for the user", description = "Generates a list of unapplied offers for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully generated daily unapplied offers"),
            @ApiResponse(responseCode = "404", description = "No unapplied offers found")
    })
    public String getDailyUnappliedOffers(@RequestHeader("userId") String userId, org.springframework.ui.Model model) {
        List<OfferDTO> offers = userOfferFacade.getNotAppliedOffersForUser(userId);
        if (offers.isEmpty()) {
            return "No unapplied offers found.";
        }
        model.addAttribute("offers", offers);
        return "daily-job-offers";
    }



}

