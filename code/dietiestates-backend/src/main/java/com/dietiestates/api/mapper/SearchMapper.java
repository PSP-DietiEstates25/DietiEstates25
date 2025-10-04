package com.dietiestates.api.mapper;

import com.dietiestates.api.dto.request.SearchRequest;
import com.dietiestates.api.dto.response.SearchResponse;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.spec.SearchSpec;

public interface SearchMapper {

	SearchSpec toSpec(SearchRequest request);
	
	SearchResponse fromEntity(Search search);
}
