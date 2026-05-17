package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.dto.useroffer.UserOfferStatusDTO;
import com.example.offerbrowserprototype.infrastructure.facade.UserOfferFacade;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-offers")
@RequiredArgsConstructor
public class UserOfferController {

    private final UserOfferFacade userOfferFacade;

    @PostMapping("/{offerId}/apply")
    @Operation(summary = "Apply to offer")
    public ResponseEntity<UserOfferStatusDTO> apply(
            @RequestHeader Long userId,
            @PathVariable Long offerId
    ) {
        return ResponseEntity.ok(
                userOfferFacade.applyToOffer(userId, offerId)
        );
    }

    @GetMapping("/not-applied")
    @Operation(summary = "Get not applied offers")
    public ResponseEntity<List<OfferDTO>> notApplied(
            @RequestHeader Long userId
    ) {
        List<OfferDTO> offers = userOfferFacade.getNotAppliedOffersForUser(userId);
        return offers.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(offers);
    }

    @GetMapping("/applied")
    @Operation(summary = "Get applied offers")
    public ResponseEntity<List<OfferDTO>> applied(
            @RequestHeader Long userId
    ) {
        List<OfferDTO> offers = userOfferFacade.getAppliedOffersForUser(userId);
        return offers.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(offers);
    }

    @GetMapping("/applied/count")
    @Operation(summary = "Count applied offers")
    public ResponseEntity<Long> count(
            @RequestHeader Long userId
    ) {
        return ResponseEntity.ok(
                userOfferFacade.countAppliedOffersForUser(userId)
        );
    }

    @PostMapping("/{offerId}/useless")
    @Operation(summary = "Mark offer as useless")
    public ResponseEntity<Void> markUseless(
            @RequestHeader Long userId,
            @PathVariable Long offerId
    ) {
        userOfferFacade.markAsUseless(userId, offerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/useless")
    @Operation(summary = "Get useless offers")
    public ResponseEntity<List<OfferDTO>> useless(
            @RequestHeader Long userId
    ) {
        List<OfferDTO> offers = userOfferFacade.getUselessOffersForUser(userId);
        return offers.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(offers);
    }
}
