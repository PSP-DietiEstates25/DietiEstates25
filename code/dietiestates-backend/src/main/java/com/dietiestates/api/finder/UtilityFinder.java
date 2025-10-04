package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.UtilityNotFoundException;
import com.dietiestates.api.model.Utility;

public interface UtilityFinder {

	Utility getUtilityById(Long id)
			throws UtilityNotFoundException;
}
