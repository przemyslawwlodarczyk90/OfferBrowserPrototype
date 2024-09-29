package com.example.offerbrowserprototype.external;

import com.example.offerbrowserprototype.domain.offer.dto.OfferDTO;
import java.util.List;

public interface JobOfferProvider {
    List<OfferDTO> fetchOffers();
    String getProviderName();
}
