package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.GeographicalPositionNotFoundException;
import com.dietiestates.api.model.GeographicalPosition;

public interface GeographicalPositionFinder {

	GeographicalPosition getGeographicalPositionById(Long id)
			throws GeographicalPositionNotFoundException;
	
}
