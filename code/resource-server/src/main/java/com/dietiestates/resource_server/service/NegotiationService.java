package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.spec.NegotiationSpec;

public interface NegotiationService {

    Negotiation setupNegotiation(NegotiationSpec negotiationSpec);
    Negotiation createNegotiation(NegotiationSpec negotiationSpec);
    Negotiation getNegotiationByUserAndRealEstate(NegotiationSpec negotiationSpec);
}
