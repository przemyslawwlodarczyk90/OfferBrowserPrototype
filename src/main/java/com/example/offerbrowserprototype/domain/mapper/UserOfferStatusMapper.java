package com.example.offerbrowserprototype.domain.mapper;

import com.example.offerbrowserprototype.domain.dto.useroffer.UserOfferStatusDTO;
import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferStatus;
import com.example.offerbrowserprototype.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserOfferStatusMapper {

    public UserOfferStatusDTO toDTO(UserOfferStatus status) {
        return new UserOfferStatusDTO(
                status.getId(),
                status.getUser().getId(),      // ⬅️ z relacji
                status.getOffer().getId(),     // ⬅️ z relacji
                status.isApplied(),
                status.getAppliedAt()
        );
    }

    /**
     * UWAGA:
     * Mapper NIE pobiera encji z DB.
     * Encje User i Offer MUSZĄ być ustawione wcześniej (np. w handlerze).
     */
    public UserOfferStatus toEntity(
            UserOfferStatusDTO dto,
            User user,
            Offer offer
    ) {
        UserOfferStatus status = new UserOfferStatus();
        status.setUser(user);
        status.setOffer(offer);
        status.setApplied(dto.isApplied());
        status.setAppliedAt(dto.getAppliedAt());
        return status;
    }
}
