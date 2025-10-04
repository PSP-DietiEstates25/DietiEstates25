package com.dietiestates.api.finder;

import com.dietiestates.api.exception.notfound.SearchNotFoundException;
import com.dietiestates.api.model.Search;

public interface SearchFinder {

	Search getSearchById(Long id)
			throws SearchNotFoundException;
}
