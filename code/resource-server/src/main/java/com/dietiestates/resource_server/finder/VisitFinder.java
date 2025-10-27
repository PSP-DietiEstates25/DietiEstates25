package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.VisitNotFoundException;
import com.dietiestates.resource_server.model.Visit;

public interface VisitFinder {

	Visit getVisitById(Long id)
			throws VisitNotFoundException;
}
