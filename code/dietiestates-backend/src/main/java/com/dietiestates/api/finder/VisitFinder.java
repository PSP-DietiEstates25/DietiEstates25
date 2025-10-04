package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.VisitNotFoundException;
import com.dietiestates.api.model.Visit;

public interface VisitFinder {

	Visit getVisitById(Long id)
			throws VisitNotFoundException;
}
