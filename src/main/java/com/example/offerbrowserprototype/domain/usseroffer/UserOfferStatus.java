package com.example.offerbrowserprototype.domain.usseroffer;

import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_offer_status",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_offer",
                        columnNames = {"user_id", "offer_id"}
                )
        },
        indexes = {
                @Index(name = "idx_user_offer_user", columnList = "user_id"),
                @Index(name = "idx_user_offer_offer", columnList = "offer_id")
        }
)
@Data
@NoArgsConstructor
public class UserOfferStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offer_id", nullable = false)
    private Offer offer;

    @Column(nullable = false)
    private boolean applied;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(nullable = false)
    private boolean useless = false;

    @Column(name = "useless_at")
    private LocalDateTime uselessAt;

    public UserOfferStatus(User user, Offer offer, boolean applied) {
        this.user = user;
        this.offer = offer;
        this.applied = applied;
        this.appliedAt = applied ? LocalDateTime.now() : null;
    }
}
