package com.dietiestates.api.mapper;

import java.util.List;

import com.dietiestates.api.dto.request.RealEstateRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.spec.RealEstateSpec;

public interface RealEstateMapper {

	RealEstateSpec toSpec(RealEstateRequest request);
	
	RealEstateResponse fromEntity(RealEstate realEstate);
	
	List<RealEstateResponse> createRealEsatatesResponse(List<RealEstate> realEsates);
}
