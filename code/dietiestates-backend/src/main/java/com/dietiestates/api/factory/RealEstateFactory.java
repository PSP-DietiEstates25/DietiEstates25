package com.dietiestates.api.factory;

import com.dietiestates.api.model.EstateAgent;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.spec.RealEstateSpec;

public interface RealEstateFactory {
	
	RealEstate createRealEstateFromSpec(
			RealEstateSpec spec,
			EstateAgent estateAgent
			);
	
}
