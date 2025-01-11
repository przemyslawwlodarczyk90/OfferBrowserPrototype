package com.example.offerbrowserprototype.domain.usseroffer;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "user_offer_status")
@Data
@NoArgsConstructor
public class UserOfferStatus {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String offerId;

    private boolean applied;

    private LocalDateTime appliedAt;

    public UserOfferStatus(String userId, String offerId, boolean applied) {
        this.userId = userId;
        this.offerId = offerId;
        this.applied = applied;
        this.appliedAt = applied ? LocalDateTime.now() : null;
    }
}
