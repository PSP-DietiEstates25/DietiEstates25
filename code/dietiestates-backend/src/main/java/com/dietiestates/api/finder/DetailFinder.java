package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.DetailNotFoundException;
import com.dietiestates.api.model.Detail;

public interface DetailFinder {

	Detail getDetailById(Long id)
			throws DetailNotFoundException;
}
