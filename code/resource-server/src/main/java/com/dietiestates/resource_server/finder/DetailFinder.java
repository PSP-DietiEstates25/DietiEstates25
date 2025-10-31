package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.DetailNotFoundException;
import com.dietiestates.resource_server.model.Detail;

public interface DetailFinder {

	Detail getDetailById(Long id)
			throws DetailNotFoundException;
}
