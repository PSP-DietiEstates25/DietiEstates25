package com.dietiestates.resource_server.factorydefaultimpl;

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
            User user,
            RealEstate realEstate,
            Offer counteredOffer
    ) {
        return Offer.builder()
                .category(spec.getCategory())
                .status(spec.getStatus())
                .user(user)
                .realEstate(realEstate)
                .amount(spec.getAmount())
                .counteredOffer(counteredOffer)
                .build();
    }

}
