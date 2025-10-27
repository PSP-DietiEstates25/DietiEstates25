package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.SearchNotFoundException;
import com.dietiestates.resource_server.model.Search;

public interface SearchFinder {

	Search getSearchById(Long id)
			throws SearchNotFoundException;
}
