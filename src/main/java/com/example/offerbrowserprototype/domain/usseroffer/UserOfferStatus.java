package com.example.offerbrowserprototype.domain.usseroffer;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "user_offer_status")
@CompoundIndex(name = "user_offer_idx", def = "{'userId': 1, 'offerId': 1}")
@Data
@NoArgsConstructor
public class UserOfferStatus {

    @Id
    private String id;

    private String userId;

    private String offerId;

    private boolean applied;

    private LocalDateTime appliedAt;

    public UserOfferStatus(String userId, String offerId, boolean applied) {
        this.userId = userId;
        this.offerId = offerId;
        this.appliedAt = applied ? LocalDateTime.now() : null;
    }
}
