package com.dietiestates.resourceserver.mapper;

import java.util.List;

import com.dietiestates.resourceserver.dto.request.RealEstateRequest;
import com.dietiestates.resourceserver.dto.response.RealEstateResponse;
import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.spec.RealEstateSpec;

public interface RealEstateMapper {

	RealEstateSpec toSpec(RealEstateRequest request);
	
	RealEstateResponse fromEntity(RealEstate realEstate);
	
	List<RealEstateResponse> createRealEsatatesResponse(List<RealEstate> realEsates);
}
