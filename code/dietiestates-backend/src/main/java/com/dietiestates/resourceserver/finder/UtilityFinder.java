package com.dietiestates.resourceserver.finder;

import com.dietiestates.resourceserver.exception.notfound.UtilityNotFoundException;
import com.dietiestates.resourceserver.model.Utility;

public interface UtilityFinder {

	Utility getUtilityById(Long id)
			throws UtilityNotFoundException;
}
