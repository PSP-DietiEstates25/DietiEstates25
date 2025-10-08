package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.OfferFactory;
import com.dietiestates.api.model.Offer;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.User;
import com.dietiestates.api.spec.OfferSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OfferFactoryImpl implements OfferFactory {

	@Override
	public Offer createOfferFromSpec(
			OfferSpec spec,
			User user,
			RealEstate realEstate
			) {
		return Offer.builder()
				.category(spec.getCategory())
				.status(spec.getStatus())
				.user(user)
				.realEstate(realEstate)
				.amount(spec.getAmount())
				.build();
	}

}
