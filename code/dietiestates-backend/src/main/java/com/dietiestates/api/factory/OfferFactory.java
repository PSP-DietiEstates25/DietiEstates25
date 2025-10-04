package com.dietiestates.api.factory;

import com.dietiestates.api.model.Offer;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.User;
import com.dietiestates.api.spec.OfferSpec;

public interface OfferFactory {

	Offer createOfferFromSpec(
			OfferSpec spec,
			User user,
			RealEstate realEstate
			);
}
