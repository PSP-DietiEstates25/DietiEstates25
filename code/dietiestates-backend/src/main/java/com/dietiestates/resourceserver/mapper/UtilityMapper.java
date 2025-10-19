package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.UtilityRequest;
import com.dietiestates.resourceserver.dto.response.UtilityResponse;
import com.dietiestates.resourceserver.model.Utility;
import com.dietiestates.resourceserver.spec.UtilitySpec;

public interface UtilityMapper {

	UtilitySpec toSpec(UtilityRequest request);
	
	UtilityResponse fromEntity(Utility utility);
}
