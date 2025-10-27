package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.GeographicalPositionNotFoundException;
import com.dietiestates.resource_server.model.GeographicalPosition;

public interface GeographicalPositionFinder {

	GeographicalPosition getGeographicalPositionById(Long id)
			throws GeographicalPositionNotFoundException;
	
}
