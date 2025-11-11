package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.UtilityNotFoundException;
import com.dietiestates.resource_server.model.Utility;

public interface UtilityFinder {
	Utility getUtilityById(Long id) throws UtilityNotFoundException;
}
