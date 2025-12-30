package com.example.offerbrowserprototype.domain.aplicationnote;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_notes",
        indexes = {
                @Index(name = "idx_app_notes_user", columnList = "user_id"),
                @Index(name = "idx_app_notes_offer", columnList = "offer_id")
        })
public class ApplicationNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "offer_id")
    private Long offerId;

    @Column(nullable = false)
    private String companyName;

    @Column(name = "offer_url")
    private String offerUrl;

    @Column(nullable = false)
    private LocalDateTime appliedAt;
}
