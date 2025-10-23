package com.dietiestates.resourceserver.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.RealEstateFactory;
import com.dietiestates.resourceserver.model.CadastralData;
import com.dietiestates.resourceserver.model.Detail;
import com.dietiestates.resourceserver.model.EstateAgent;
import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.spec.RealEstateSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RealEstateFactoryImpl implements RealEstateFactory {

	@Override
	public RealEstate createRealEstateFromSpec(
			RealEstateSpec spec,
			EstateAgent estateAgent,
			CadastralData cadastralData,
			Detail detail
			) {
		return RealEstate.builder()
				.category(spec.getCategory())
				.images(spec.getImages())
				.description(spec.getDescription())
				.estateAgent(estateAgent)
				.cadastralData(cadastralData)
				.detail(detail)
				.build();
	}

}
