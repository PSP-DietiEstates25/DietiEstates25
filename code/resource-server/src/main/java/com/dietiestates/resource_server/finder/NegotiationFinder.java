package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.NegotiationNotFoundException;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.spec.NegotiationSpec;

public interface NegotiationFinder {
    Negotiation getRealEstateUserNegotiation(Long realEstateId, Long userId) throws NegotiationNotFoundException;
    Negotiation getRealEstateEstateAgentNegotiation(Long realEstateId, Long estateAgentId) throws NegotiationNotFoundException;
}
