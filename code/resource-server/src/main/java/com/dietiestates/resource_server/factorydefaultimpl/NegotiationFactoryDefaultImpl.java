package com.dietiestates.resource_server.factorydefaultimpl;

import com.dietiestates.resource_server.factory.NegotiationFactory;
import com.dietiestates.resource_server.model.EstateAgent;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.User;
import org.springframework.stereotype.Component;

@Component
public class NegotiationFactoryDefaultImpl implements NegotiationFactory {

    @Override
    public Negotiation createNegotiationFromSpec(
            User user,
            EstateAgent estateAgent,
            RealEstate realEstate
    ) {
        return Negotiation.builder()
                .user(user)
                .estateAgent(estateAgent)
                .realEstate(realEstate)
                .build();
    }
}
