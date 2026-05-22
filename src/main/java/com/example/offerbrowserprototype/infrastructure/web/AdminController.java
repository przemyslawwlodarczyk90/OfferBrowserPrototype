package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.admin.AdminOfferMarkerDTO;
import com.example.offerbrowserprototype.domain.dto.admin.AdminUserDTO;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferStatus;
import com.example.offerbrowserprototype.infrastructure.repository.ApplicationNoteRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserOfferStatusRepository;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
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

    public AdminController(UserRepository userRepository,
                           UserOfferStatusRepository userOfferStatusRepository,
                           ApplicationNoteRepository applicationNoteRepository) {
        this.userRepository = userRepository;
        this.userOfferStatusRepository = userOfferStatusRepository;
        this.applicationNoteRepository = applicationNoteRepository;
    }

    @Operation(summary = "List all users with their application statistics")
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
                        .uselessCount(userOfferStatusRepository.countByUser_IdAndUselessTrue(user.getId()))
                        .notesCount(applicationNoteRepository.countByUserId(user.getId()))
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "List IDs of offers that at least one user marked as useless")
    @GetMapping("/offers/useless-ids")
    public ResponseEntity<List<Long>> getUselessOfferIds() {
        return ResponseEntity.ok(userOfferStatusRepository.findOfferIdsWithAnyUseless());
    }

    @Operation(summary = "List users who marked an offer as useless")
    @GetMapping("/offers/{offerId}/markers")
    public ResponseEntity<List<AdminOfferMarkerDTO>> getOfferMarkers(@PathVariable Long offerId) {
        List<UserOfferStatus> statuses = userOfferStatusRepository.findByOffer_IdAndUselessTrue(offerId);
        List<AdminOfferMarkerDTO> markers = statuses.stream()
                .map(s -> AdminOfferMarkerDTO.builder()
                        .userId(s.getUser().getId())
                        .username(s.getUser().getUsername())
                        .email(s.getUser().getEmail())
                        .uselessAt(s.getUselessAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(markers);
    }

}
