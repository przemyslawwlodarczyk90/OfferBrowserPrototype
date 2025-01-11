package com.example.offerbrowserprototype.infrastructure.facade;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.domain.dto.useroffer.UserOfferStatusDTO;
import com.example.offerbrowserprototype.domain.mapper.OfferMapper;
import com.example.offerbrowserprototype.domain.mapper.UserOfferStatusMapper;
import com.example.offerbrowserprototype.domain.offer.Offer;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferApplyHandler;
import com.example.offerbrowserprototype.domain.usseroffer.UserOfferQueryHandler;
import com.example.offerbrowserprototype.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserOfferFacade {

    private final UserOfferApplyHandler applyHandler;
    private final UserRepository userRepository;
    private final UserOfferStatusMapper userOfferStatusMapper;
    private final OfferMapper offerMapper; // Mapper ofert
    private final UserOfferQueryHandler queryHandler;

    public UserOfferFacade(UserOfferApplyHandler applyHandler,
                           UserRepository userRepository,
                           UserOfferStatusMapper userOfferStatusMapper,
                           OfferMapper offerMapper, // Mapper do konwersji encji Offer na OfferDTO
                           UserOfferQueryHandler queryHandler) {
        this.applyHandler = applyHandler;
        this.userRepository = userRepository;
        this.userOfferStatusMapper = userOfferStatusMapper;
        this.offerMapper = offerMapper;
        this.queryHandler = queryHandler;
    }

    // Metoda aplikacji oferty dla użytkownika na podstawie emaila
    public UserOfferStatusDTO applyToOfferByEmail(String email, String offerId) {
        String userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"))
                .getId();

        var userOfferStatus = applyHandler.applyToOffer(userId, offerId);

        return userOfferStatusMapper.toDTO(userOfferStatus); // Konwersja UserOfferStatus na DTO
    }

    // Pobierz oferty niezaplikowane dla danego użytkownika
    public List<OfferDTO> getNotAppliedOffersForUser(String userId) {
        // Pobieranie ofert bez statusów "applied" dla użytkownika
        List<Offer> unappliedOffers = queryHandler.getNotAppliedOffersForUser(userId);
        return unappliedOffers.stream()
                .map(offerMapper::toDTO) // Konwersja encji Offer na DTO
                .toList();
    }
}
