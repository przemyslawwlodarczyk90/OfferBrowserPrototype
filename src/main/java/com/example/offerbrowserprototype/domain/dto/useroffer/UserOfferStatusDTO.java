package com.example.offerbrowserprototype.domain.dto.useroffer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserOfferStatusDTO {
    private String userId;
    private String offerId;
    private boolean applied;
    private LocalDateTime appliedAt;
}
