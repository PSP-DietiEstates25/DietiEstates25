package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.UtilityRequest;
import com.dietiestates.api.dto.response.UtilityResponse;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.spec.UtilitySpec;

public interface UtilityMapper {

	UtilitySpec toSpec(UtilityRequest request);
	
	UtilityResponse fromEntity(Utility utility);
}
