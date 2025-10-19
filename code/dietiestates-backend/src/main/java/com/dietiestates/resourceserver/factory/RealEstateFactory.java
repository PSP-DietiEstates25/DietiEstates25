package com.dietiestates.resourceserver.factory;

import com.dietiestates.resourceserver.model.CadastralData;
import com.dietiestates.resourceserver.model.Detail;
import com.dietiestates.resourceserver.model.EstateAgent;
import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.spec.RealEstateSpec;

public interface RealEstateFactory {
	
	RealEstate createRealEstateFromSpec(
			RealEstateSpec spec,
			EstateAgent estateAgent,
			CadastralData cadastralData,
			Detail detail
			);
	
}
