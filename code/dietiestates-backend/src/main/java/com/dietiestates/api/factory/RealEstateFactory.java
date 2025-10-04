package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.RealEstateRequest;
import com.dietiestates.api.model.RealEstate;

public interface RealEstateFactory {
	
	RealEstate createRealEstate(RealEstateRequest realEstate);
	
}
