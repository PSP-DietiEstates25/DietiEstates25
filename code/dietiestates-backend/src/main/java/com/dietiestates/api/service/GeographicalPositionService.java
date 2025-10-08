package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.GeographicalPositionRequest;
import com.dietiestates.api.dto.response.GeographicalPositionResponse;

public interface GeographicalPositionService {

	void createGeographicalPosition(GeographicalPositionRequest request);
	
	GeographicalPositionResponse getGeographicalPositionById(Long geographicalPositionId);
}
