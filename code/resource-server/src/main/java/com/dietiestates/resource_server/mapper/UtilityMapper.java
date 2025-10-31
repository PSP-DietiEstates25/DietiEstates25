package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.UtilityRequest;
import com.dietiestates.resource_server.dto.response.UtilityResponse;
import com.dietiestates.resource_server.model.Utility;
import com.dietiestates.resource_server.spec.UtilitySpec;

public interface UtilityMapper {

	UtilitySpec toSpec(UtilityRequest request);
	
	UtilityResponse fromEntity(Utility utility);
}
