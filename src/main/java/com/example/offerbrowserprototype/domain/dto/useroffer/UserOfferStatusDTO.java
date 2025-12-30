package com.example.offerbrowserprototype.domain.dto.useroffer;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class UserOfferStatusDTO {

    private Long id;
    private Long userId;
    private Long offerId;
    private boolean applied;
    private LocalDateTime appliedAt;

    public UserOfferStatusDTO(
            Long id,
            Long userId,
            Long offerId,
            boolean applied,
            LocalDateTime appliedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.offerId = offerId;
        this.applied = applied;
        this.appliedAt = appliedAt;
    }

    public UserOfferStatusDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOfferId() {
        return offerId;
    }

    public boolean isApplied() {
        return applied;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }
}
