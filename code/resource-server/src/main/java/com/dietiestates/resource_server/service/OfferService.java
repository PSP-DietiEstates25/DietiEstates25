package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.spec.OfferSpec;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;


public interface OfferService {

	OfferResponse createUserOffer(OfferRequest request, Long realEstateId, String userEmail);
    OfferResponse createEstateAgentCounterOffer(OfferRequest request, Long realEstateId, String estateAgentEmail);
    OfferResponse getOfferById(Long realEstateId, Long offerId);
    Page<OfferResponse> getPagedUserRealEstateOffers(Long realEstateId, String userEmail, Integer page, Integer size);
    Page<OfferResponse> getPagedEstateAgentRealEstateOffers(Long realEstateId, String estateAgentEmail, Integer page, Integer size);
    OfferResponse updateOfferStatus(OfferRequest request, Long realEstateId, Long offerId);
}
