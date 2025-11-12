package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.GeographicalPositionNotFoundException;
import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.repository.GeographicalPositionRepository;

public interface GeographicalPositionFinder {
	GeographicalPosition getGeographicalPositionById(Long id) throws GeographicalPositionNotFoundException;
}
