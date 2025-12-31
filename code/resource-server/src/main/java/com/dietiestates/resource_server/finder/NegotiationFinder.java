package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.NegotiationNotFoundException;
import com.dietiestates.resource_server.model.Negotiation;

import java.util.List;

public interface NegotiationFinder {
    Negotiation getNegotiationById(Long id) throws NegotiationNotFoundException;
    Negotiation getRealEstateUserNegotiation(Long realEstateId, Long userId) throws NegotiationNotFoundException;
    Negotiation getRealEstateEstateAgentNegotiation(Long realEstateId, Long estateAgentId) throws NegotiationNotFoundException;
    List<Negotiation> getAllUserNegotiationsForActiveRealEstates(Long userId);
    List<Negotiation> getAllUserNegotiationsForAllRealEstates(Long userId);
    List<Negotiation> getAllEstateAgentNegotiationsForActiveRealEstates(Long estateAgentId);
    List<Negotiation> getAllEstateAgentNegotiationsForAllRealEstates(Long estateAgentId);
}
