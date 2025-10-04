package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.RealEstateRequest;
import com.dietiestates.api.factory.RealEstateFactory;
import com.dietiestates.api.model.RealEstate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RealEstateFactoryImpl implements RealEstateFactory {

	@Override
	public RealEstate createRealEstate(RealEstateRequest realEstate) {
		// TODO Auto-generated method stub
		return null;
	}

}
