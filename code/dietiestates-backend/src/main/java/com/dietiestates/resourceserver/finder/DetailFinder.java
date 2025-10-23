package com.dietiestates.resourceserver.finder;

import com.dietiestates.resourceserver.exception.notfound.DetailNotFoundException;
import com.dietiestates.resourceserver.model.Detail;

public interface DetailFinder {

	Detail getDetailById(Long id)
			throws DetailNotFoundException;
}
