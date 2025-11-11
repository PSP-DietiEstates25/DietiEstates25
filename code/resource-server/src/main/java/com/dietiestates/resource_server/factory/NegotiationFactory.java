package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.User;

public interface NegotiationFactory {
    Negotiation createNegotiationFromSpec(User user, EstateAgent estateAgent, RealEstate realEstate);
}
