package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.dto.useroffer.UserOfferStatusDTO;
import com.example.offerbrowserprototype.domain.exception.OfferAlreadyAppliedException;
import com.example.offerbrowserprototype.infrastructure.facade.UserOfferFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-offers")
@Tag(name = "User Offers", description = "Manage user-specific offers")
@RequiredArgsConstructor
public class UserOfferController {

    private final UserOfferFacade userOfferFacade;
    private static final Logger logger = LoggerFactory.getLogger(UserOfferController.class);

    @Operation(summary = "Apply to an offer", description = "Marks an offer as applied for the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully applied to the offer"),
            @ApiResponse(responseCode = "404", description = "Offer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @GetMapping("/{offerId}/apply")
    public String applyToOffer(
            @RequestParam(value = "email", required = false) String email,
            @PathVariable String offerId,
            Model model) {
        logger.info("Received request to apply to offer. email: {}, offerId: {}", email, offerId);

        if (email == null || email.isEmpty()) {
            logger.error("Invalid input: email is null or empty");
            model.addAttribute("message", "Invalid input data. Please provide a valid email.");
            return "error";
        }

        try {
            UserOfferStatusDTO userOfferStatusDTO = userOfferFacade.applyToOfferByEmail(email, offerId);

            if (userOfferStatusDTO.isApplied()) {
                model.addAttribute("message", "You have successfully applied to the offer!");
                return "success";
            }

        } catch (OfferAlreadyAppliedException e) {
            logger.warn("Offer already applied for email: {}, offerId: {}", email, offerId);
            model.addAttribute("message", "This offer has already been applied!");
            return "already applied before to this offer";

        } catch (Exception e) {
            logger.error("Error occurred while applying to offer. email: {}, offerId: {}, error: {}", email, offerId, e.getMessage());
            model.addAttribute("message", "An error occurred: " + e.getMessage());
            return "error";
        }
        return "error";
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

    @GetMapping("/applied/count")
    @Operation(summary = "Get count of applied offers", description = "Retrieves the count of offers the user has applied to.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved count of applied offers")
    })
    public ResponseEntity<Long> countAppliedOffers(@RequestHeader("userId") String userId) {
        long count = userOfferFacade.countAppliedOffersForUser(userId);
        return ResponseEntity.ok(count);
    }

}
