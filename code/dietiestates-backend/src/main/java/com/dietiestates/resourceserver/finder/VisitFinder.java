package com.dietiestates.resourceserver.finder;

import com.dietiestates.resourceserver.exception.notfound.VisitNotFoundException;
import com.dietiestates.resourceserver.model.Visit;

public interface VisitFinder {

	Visit getVisitById(Long id)
			throws VisitNotFoundException;
}
