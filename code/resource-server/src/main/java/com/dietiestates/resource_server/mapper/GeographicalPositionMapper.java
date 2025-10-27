package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.GeographicalPositionRequest;
import com.dietiestates.resource_server.dto.response.GeographicalPositionResponse;
import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.spec.GeographicalPositionSpec;

public interface GeographicalPositionMapper {

	GeographicalPositionSpec toSpec(GeographicalPositionRequest request);
	
	GeographicalPositionResponse fromEntity(GeographicalPosition geographicalPosition);
}
