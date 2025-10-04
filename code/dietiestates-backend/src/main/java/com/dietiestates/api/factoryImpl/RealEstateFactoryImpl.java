package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.factory.RealEstateFactory;
import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.spec.RealEstateSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RealEstateFactoryImpl implements RealEstateFactory {

	@Override
	public RealEstate createRealEstateFromSpec(
			RealEstateSpec spec,
			EstateAgent estateAgent
			) {
		return RealEstate.builder()
				.category(AdCategory.valueOf(spec.getCategory()))
				.images(spec.getImages())
				.description(spec.getDescription())
				.estateAgent(estateAgent)
				.build();
	}

}
