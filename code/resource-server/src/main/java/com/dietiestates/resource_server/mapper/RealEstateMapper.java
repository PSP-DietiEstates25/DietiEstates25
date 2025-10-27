package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.RealEstateRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.spec.RealEstateSpec;

import java.util.List;

public interface RealEstateMapper {

	RealEstateSpec toSpec(RealEstateRequest request);
	
	RealEstateResponse fromEntity(RealEstate realEstate);
	
	List<RealEstateResponse> createRealEsatatesResponse(List<RealEstate> realEsates);
}
