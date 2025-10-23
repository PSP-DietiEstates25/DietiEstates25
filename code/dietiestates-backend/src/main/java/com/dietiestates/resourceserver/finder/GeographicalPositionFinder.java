package com.dietiestates.resourceserver.finder;

import com.dietiestates.resourceserver.exception.notfound.GeographicalPositionNotFoundException;
import com.dietiestates.resourceserver.model.GeographicalPosition;

public interface GeographicalPositionFinder {

	GeographicalPosition getGeographicalPositionById(Long id)
			throws GeographicalPositionNotFoundException;
	
}
