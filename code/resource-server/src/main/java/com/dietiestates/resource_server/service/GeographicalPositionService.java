package com.dietiestates.resource_server.service;

import com.dietiestates.resource_server.dto.request.GeographicalPositionRequest;
import com.dietiestates.resource_server.dto.response.GeographicalPositionResponse;

public interface GeographicalPositionService {
	GeographicalPositionResponse createGeographicalPosition(GeographicalPositionRequest request);
	GeographicalPositionResponse getGeographicalPositionById(Long geographicalPositionId);
    GeographicalPositionResponse getDetailGeograpicalPosition(Long detailId);
    void updateGeographicalPosition(Long geographicalPositionId, GeographicalPositionRequest request);
}
