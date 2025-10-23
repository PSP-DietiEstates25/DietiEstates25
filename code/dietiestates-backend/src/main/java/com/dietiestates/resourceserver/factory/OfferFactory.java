package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.Offer;
import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.model.User;
import com.dietiestates.resourceserver.spec.OfferSpec;

public interface OfferFactory {

	Offer createOfferFromSpec(
			OfferSpec spec,
			User user,
			RealEstate realEstate
			);
}
