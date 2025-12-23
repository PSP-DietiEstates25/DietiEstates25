package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.Visit;
import com.dietiestates.resource_server.spec.VisitSpec;

public interface VisitFactory {
    Visit createVisitFromSpec(VisitSpec spec, Negotiation negotiation);
}
