package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.OfferRequest;
import com.dietiestates.api.factory.OfferFactory;
import com.dietiestates.api.model.Offer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OfferFactoryImpl implements OfferFactory {

	@Override
	public Offer createOffer(OfferRequest request, Long realEstateId) {
		// TODO Auto-generated method stub
		return null;
	}

}
