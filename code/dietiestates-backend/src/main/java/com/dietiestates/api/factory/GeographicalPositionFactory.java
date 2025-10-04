package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.GeographicalPositionRequest;
import com.dietiestates.api.model.GeographicalPosition;

public interface GeographicalPositionFactory {

	GeographicalPosition createGeographicalPosition(GeographicalPositionRequest request, Long detailId);
}
