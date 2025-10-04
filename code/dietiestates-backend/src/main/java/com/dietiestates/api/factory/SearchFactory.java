package com.dietiestates.api.factory;

import com.dietiestates.api.dto.request.SearchRequest;
import com.dietiestates.api.model.Search;

public interface SearchFactory {

	Search createSearch(SearchRequest request);
}
