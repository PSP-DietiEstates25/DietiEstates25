package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.OfferRequest;
import com.dietiestates.resource_server.dto.response.OfferResponse;
import com.dietiestates.resource_server.spec.OfferSpec;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;


public interface OfferService {
	OfferResponse createOffer(OfferRequest request, Long realEstateId, String creatorEmail, String creatorRole);
    OfferResponse createUserOffer(OfferRequest request, Long realEstateId, String userEmail);
    OfferResponse createEstateAgentCounterOffer(OfferRequest request, Long realEstateId, String estateAgentEmail);
    OfferResponse getOfferById(Long realEstateId, Long offerId);
    Page<OfferResponse> getRealEstateOffers(Long realEstateId, String creatorEmail, String creatorRole, Integer page, Integer size);
    Page<OfferResponse> getRealEstateUserOffers(Long realEstateId, String userEmail, Integer page, Integer size);
    Page<OfferResponse> getRealEstateEstateAgentOffers(Long realEstateId, String estateAgentEmail, Integer page, Integer size);
    Page<OfferResponse> getOffers(String creatorEmail, String creatorRole, String status, Integer page, Integer size);
    Page<OfferResponse> getAllUserOffers(String userEmail, String status, Integer page, Integer size);
    Page<OfferResponse> getAllEstateAgentOffers(String estateAgentEmail, String status, Integer page, Integer size);
    OfferResponse updateOfferStatus(OfferRequest request, Long realEstateId, Long offerId);
}
