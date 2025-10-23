package com.dietiestates.resourceserver.finder;

import com.dietiestates.resourceserver.exception.notfound.SearchNotFoundException;
import com.dietiestates.resourceserver.model.Search;

public interface SearchFinder {

	Search getSearchById(Long id)
			throws SearchNotFoundException;
}
