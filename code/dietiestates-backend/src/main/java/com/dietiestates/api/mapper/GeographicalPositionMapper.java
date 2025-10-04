package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.GeographicalPositionRequest;
import com.dietiestates.api.dto.response.GeographicalPositionResponse;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.spec.GeographicalPositionSpec;

public interface GeographicalPositionMapper {

	GeographicalPositionSpec toSpec(GeographicalPositionRequest request);
	
	GeographicalPositionResponse fromEntity(GeographicalPosition geographicalPosition);
}
