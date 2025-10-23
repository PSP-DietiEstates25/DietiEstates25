package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.OfferFactory;
import com.dietiestates.resourceserver.model.Offer;
import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.model.User;
import com.dietiestates.resourceserver.spec.OfferSpec;

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
