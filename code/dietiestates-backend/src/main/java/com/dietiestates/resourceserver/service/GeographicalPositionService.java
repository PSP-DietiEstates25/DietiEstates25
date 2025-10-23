package com.dietiestates.resourceserver.service;

import com.dietiestates.resourceserver.dto.request.GeographicalPositionRequest;
import com.dietiestates.resourceserver.dto.response.GeographicalPositionResponse;

public interface GeographicalPositionService {

	GeographicalPositionResponse createGeographicalPosition(GeographicalPositionRequest request);
	
	GeographicalPositionResponse getGeographicalPositionById(Long geographicalPositionId);
}
