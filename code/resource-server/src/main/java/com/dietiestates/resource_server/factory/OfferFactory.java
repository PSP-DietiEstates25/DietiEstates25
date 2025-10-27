package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.User;
import com.dietiestates.resource_server.spec.OfferSpec;

public interface OfferFactory {

    Offer createOfferFromSpec(
            OfferSpec spec,
            User user,
            RealEstate realEstate
    );
}
