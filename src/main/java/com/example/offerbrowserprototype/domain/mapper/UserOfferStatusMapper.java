package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.useroffer.UserOfferStatusDTO;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferStatus;
import org.springframework.stereotype.Component;

@Component
public class UserOfferStatusMapper {

    public UserOfferStatusDTO toDTO(UserOfferStatus userOfferStatus) {
        return new UserOfferStatusDTO(
                userOfferStatus.getUserId(),
                userOfferStatus.getOfferId(),
                userOfferStatus.isApplied(),
                userOfferStatus.getAppliedAt()
        );
    }

    public UserOfferStatus toEntity(UserOfferStatusDTO dto) {
        UserOfferStatus userOfferStatus = new UserOfferStatus();
        userOfferStatus.setUserId(dto.getUserId());
        userOfferStatus.setOfferId(dto.getOfferId());
        userOfferStatus.setApplied(dto.isApplied());
        userOfferStatus.setAppliedAt(dto.getAppliedAt());
        return userOfferStatus;
    }
}
