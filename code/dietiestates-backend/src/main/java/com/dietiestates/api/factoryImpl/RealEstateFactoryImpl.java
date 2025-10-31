package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.RealEstateFactory;
import com.dietiestates.api.model.CadastralData;
import com.dietiestates.api.model.Detail;
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
			EstateAgent estateAgent,
			CadastralData cadastralData,
			Detail detail) {
		return RealEstate.builder()
				.category(spec.getCategory())
				.images(spec.getImages() != null ? spec.getImages() : null)
				.description(spec.getDescription())
				.estateAgent(estateAgent)
				.cadastralData(cadastralData)
				.detail(detail)
				.build();
	}

}
