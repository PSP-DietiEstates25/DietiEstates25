package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.NegotiationNotFoundException;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.spec.NegotiationSpec;

import java.util.List;

public interface NegotiationFinder {
    Negotiation getNegotiationById(Long id) throws NegotiationNotFoundException;
    Negotiation getRealEstateUserNegotiation(Long realEstateId, Long userId) throws NegotiationNotFoundException;
    Negotiation getRealEstateEstateAgentNegotiation(Long realEstateId, Long estateAgentId) throws NegotiationNotFoundException;
    List<Negotiation> getAllUserNegotiations(Long userId);
    List<Negotiation> getAllEstateAgentNegotiations(Long estateAgentId);
}
