package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.GeographicalPositionRequest;
import com.dietiestates.resourceserver.dto.response.GeographicalPositionResponse;
import com.dietiestates.resourceserver.model.GeographicalPosition;
import com.dietiestates.resourceserver.spec.GeographicalPositionSpec;

public interface GeographicalPositionMapper {

	GeographicalPositionSpec toSpec(GeographicalPositionRequest request);
	
	GeographicalPositionResponse fromEntity(GeographicalPosition geographicalPosition);
}
