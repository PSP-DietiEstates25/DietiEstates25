package com.dietiestates.api.service;

import com.dietiestates.api.dto.request.GeographicalPositionRequest;
import com.dietiestates.api.dto.response.GeographicalPositionResponse;

public interface GeographicalPositionService {

	void createGeographicalPosition(GeographicalPositionRequest request, Long detailId);
	
	GeographicalPositionResponse getGeographicalPositionById(Long detailId, Long geographicalPositionId);
}
