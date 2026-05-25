package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.admin.AdminOfferMarkerDTO;
import com.example.offerbrowserprototype.domain.dto.admin.AdminUserDTO;
import com.example.offerbrowserprototype.domain.offer.FlagType;
import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import com.example.offerbrowserprototype.infrastructure.repository.OfferFlagRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin-only endpoints — require ROLE_ADMIN")
public class AdminController {

    private final UserRepository userRepository;
    private final UserOfferStatusRepository userOfferStatusRepository;
    private final ApplicationNoteRepository applicationNoteRepository;
    private final OfferFlagRepository offerFlagRepository;

    public AdminController(UserRepository userRepository,
                           UserOfferStatusRepository userOfferStatusRepository,
                           ApplicationNoteRepository applicationNoteRepository,
                           OfferFlagRepository offerFlagRepository) {
        this.userRepository = userRepository;
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.applicationNoteRepository = applicationNoteRepository;
        this.offerFlagRepository = offerFlagRepository;
    }

    @Operation(summary = "List all users with statistics",
               description = "Returns every registered user with their applied, watchlist, duplicate/useless flag counts and notes counts. Requires ROLE_ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User list returned"),
            @ApiResponse(responseCode = "403", description = "Access denied — ROLE_ADMIN required")
    })
    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>> getUsers() {
        List<AdminUserDTO> users = userRepository.findAll().stream()
                .map(user -> AdminUserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole() != null ? user.getRole().name() : "USER")
                        .active(user.isActive())
                        .appliedCount(userOfferStatusRepository.countByUser_IdAndAppliedTrue(user.getId()))
                        .watchlistCount(userOfferStatusRepository.countByUser_IdAndAppliedFalse(user.getId()))
                        .uselessCount(offerFlagRepository.countByUser_IdAndType(user.getId(), FlagType.USELESS))
                        .notesCount(applicationNoteRepository.countByUserId(user.getId()))
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "List IDs of offers flagged as useless by any user")
    @GetMapping("/offers/useless-ids")
    public ResponseEntity<List<Long>> getUselessOfferIds() {
        return ResponseEntity.ok(offerFlagRepository.findFlaggedOfferIdsByType(FlagType.USELESS));
    }

    @Operation(summary = "List IDs of offers flagged as duplicate by any user")
    @GetMapping("/offers/duplicate-ids")
    public ResponseEntity<List<Long>> getDuplicateOfferIds() {
        return ResponseEntity.ok(offerFlagRepository.findFlaggedOfferIdsByType(FlagType.DUPLICATE));
    }

    @Operation(summary = "List all flags on a given offer",
               description = "Returns all DUPLICATE and USELESS flags for this offer: who set them and when. Requires ROLE_ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flag list returned (empty if none)"),
            @ApiResponse(responseCode = "403", description = "Access denied — ROLE_ADMIN required")
    })
    @GetMapping("/offers/{offerId}/markers")
    public ResponseEntity<List<AdminOfferMarkerDTO>> getOfferMarkers(@PathVariable Long offerId) {
        List<AdminOfferMarkerDTO> markers = offerFlagRepository.findByOffer_Id(offerId).stream()
                .map(f -> AdminOfferMarkerDTO.builder()
                        .userId(f.getUser().getId())
                        .username(f.getUser().getUsername())
                        .email(f.getUser().getEmail())
                        .flagType(f.getType().name())
                        .flaggedAt(f.getFlaggedAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(markers);
    }
}
