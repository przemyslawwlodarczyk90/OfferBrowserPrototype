package com.example.offerbrowserprototype.external;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import java.util.List;

public interface JobOfferProvider {
    List<OfferDTO> fetchOffers();
    String getProviderName();
}
