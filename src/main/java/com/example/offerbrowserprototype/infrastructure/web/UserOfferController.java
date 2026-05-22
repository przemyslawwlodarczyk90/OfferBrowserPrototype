package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.dto.useroffer.UserOfferStatusDTO;
import com.example.offerbrowserprototype.infrastructure.facade.UserOfferFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-offers")
@RequiredArgsConstructor
@Tag(
    name = "User Offers",
    description = "Manages a user's offer pipeline: watchlist, applications and useless offers. " +
                  "Pass the user's ID in the `userId` request header."
)
public class UserOfferController {

    private final UserOfferFacade userOfferFacade;

    @PostMapping("/{offerId}/apply")
    @Operation(summary = "Apply to an offer",
               description = "Records the user's application to the given offer.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application recorded"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Offer or user not found")
    })
    public ResponseEntity<UserOfferStatusDTO> apply(
            @Parameter(in = ParameterIn.HEADER, name = "userId", required = true,
                       description = "ID of the authenticated user",
                       schema = @Schema(type = "integer", format = "int64"))
            @RequestHeader Long userId,
            @PathVariable Long offerId
    ) {
        return ResponseEntity.ok(userOfferFacade.applyToOffer(userId, offerId));
    }

    @GetMapping("/not-applied")
    @Operation(summary = "Get watchlist (not yet applied)",
               description = "Returns all offers the user has not yet applied to.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<OfferDTO>> notApplied(
            @Parameter(in = ParameterIn.HEADER, name = "userId", required = true,
                       schema = @Schema(type = "integer", format = "int64"))
            @RequestHeader Long userId
    ) {
        return ResponseEntity.ok(userOfferFacade.getNotAppliedOffersForUser(userId));
    }

    @GetMapping("/applied")
    @Operation(summary = "Get applied offers",
               description = "Returns all offers the user has already applied to.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<OfferDTO>> applied(
            @Parameter(in = ParameterIn.HEADER, name = "userId", required = true,
                       schema = @Schema(type = "integer", format = "int64"))
            @RequestHeader Long userId
    ) {
        return ResponseEntity.ok(userOfferFacade.getAppliedOffersForUser(userId));
    }

    @GetMapping("/applied/count")
    @Operation(summary = "Count applied offers",
               description = "Returns the total number of offers the user has applied to.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Count returned"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<Long> count(
            @Parameter(in = ParameterIn.HEADER, name = "userId", required = true,
                       schema = @Schema(type = "integer", format = "int64"))
            @RequestHeader Long userId
    ) {
        return ResponseEntity.ok(userOfferFacade.countAppliedOffersForUser(userId));
    }

    @PostMapping("/{offerId}/useless")
    @Operation(summary = "Mark offer as useless",
               description = "Hides the offer from this user's view only. Other users are not affected.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Offer marked as useless"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Offer or user not found")
    })
    public ResponseEntity<Void> markUseless(
            @Parameter(in = ParameterIn.HEADER, name = "userId", required = true,
                       schema = @Schema(type = "integer", format = "int64"))
            @RequestHeader Long userId,
            @PathVariable Long offerId
    ) {
        userOfferFacade.markAsUseless(userId, offerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{offerId}/apply-email", produces = MediaType.TEXT_HTML_VALUE)
    @Operation(summary = "Apply to offer via email link",
               description = "GET endpoint for one-click apply from daily email. Returns an HTML confirmation page.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Applied — confirmation page shown"),
            @ApiResponse(responseCode = "200", description = "Already applied — info page shown")
    })
    public ResponseEntity<String> applyViaEmail(
            @RequestParam Long userId,
            @PathVariable Long offerId
    ) {
        String title;
        String message;
        try {
            UserOfferStatusDTO result = userOfferFacade.applyToOffer(userId, offerId);
            title   = "Aplikacja zapisana!";
            message = "Oferta została oznaczona jako zaaplikowana. Możesz zamknąć tę kartę.";
        } catch (Exception e) {
            title   = "Już zaaplikowano";
            message = "Ta oferta była już wcześniej oznaczona jako zaaplikowana.";
        }
        String html = """
                <!DOCTYPE html>
                <html lang="pl">
                <head><meta charset="UTF-8"><title>%s</title>
                <style>
                  body{margin:0;display:flex;align-items:center;justify-content:center;
                       min-height:100vh;font-family:'Courier New',monospace;background:#0b0f1a;color:#f0f4f8;}
                  .box{text-align:center;padding:48px 40px;background:#111827;
                       border:1px solid #2d3748;border-radius:16px;max-width:440px;}
                  .icon{font-size:3rem;margin-bottom:16px;}
                  h1{margin:0 0 12px;font-size:1.3rem;color:#4ade80;}
                  p{margin:0;font-size:.85rem;color:#9ca3af;line-height:1.6;}
                </style></head>
                <body><div class="box">
                  <div class="icon">✓</div>
                  <h1>%s</h1>
                  <p>%s</p>
                </div></body></html>
                """.formatted(title, title, message);
        return ResponseEntity.ok(html);
    }

    @GetMapping("/useless")
    @Operation(summary = "Get useless offers",
               description = "Returns all offers the user has marked as useless.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<OfferDTO>> useless(
            @Parameter(in = ParameterIn.HEADER, name = "userId", required = true,
                       schema = @Schema(type = "integer", format = "int64"))
            @RequestHeader Long userId
    ) {
        return ResponseEntity.ok(userOfferFacade.getUselessOffersForUser(userId));
    }
}
