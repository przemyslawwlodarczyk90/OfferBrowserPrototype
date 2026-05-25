package com.example.offerbrowserprototype.domain.offer;

import com.example.offerbrowserprototype.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "offer_flags",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_offer_flag_user_offer_type",
        columnNames = {"user_id", "offer_id", "flag_type"}
    ),
    indexes = {
        @Index(name = "idx_offer_flag_offer",   columnList = "offer_id"),
        @Index(name = "idx_offer_flag_user",    columnList = "user_id"),
        @Index(name = "idx_offer_flag_type",    columnList = "flag_type")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offer_id", nullable = false)
    private Offer offer;

    @Enumerated(EnumType.STRING)
    @Column(name = "flag_type", nullable = false)
    private FlagType type;

    @Column(name = "flagged_at", nullable = false)
    private LocalDateTime flaggedAt;
}
