package com.dietiestates.resource_server.factorydefaultimpl;

import com.dietiestates.resource_server.model.Negotiation;
import org.springframework.stereotype.Component;

import com.dietiestates.resource_server.factory.OfferFactory;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.spec.OfferSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OfferFactoryDefaultImpl implements OfferFactory {

    @Override
    public Offer createOfferFromSpec(
            OfferSpec spec,
            Negotiation negotiation
    ) {
        return Offer.offerBuilder()
                .category(spec.getCategory())
                .status(spec.getStatus())
                .negotiation(negotiation)
                .amount(spec.getAmount())
                .build();
    }

}
