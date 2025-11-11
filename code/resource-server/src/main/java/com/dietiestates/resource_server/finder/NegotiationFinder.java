package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.NegotiationNotFoundException;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.spec.NegotiationSpec;

public interface NegotiationFinder {
    Negotiation getNegotiationById(Long id) throws NegotiationNotFoundException;
    Negotiation getNegotiationByRealEstate(RealEstate realEstate) throws NegotiationNotFoundException;
    Negotiation getNegotiationByUserAndRealEstate(User user, RealEstate realEstate) throws NegotiationNotFoundException;
    Negotiation getNegotiationByEstateAgentAndRealEstate(EstateAgent estateAgent, RealEstate realEstate) throws NegotiationNotFoundException;
}
