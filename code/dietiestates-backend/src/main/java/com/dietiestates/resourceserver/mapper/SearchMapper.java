package com.dietiestates.resourceserver.mapper;

import com.dietiestates.resourceserver.dto.request.SearchRequest;
import com.dietiestates.resourceserver.dto.response.SearchResponse;
import com.dietiestates.resourceserver.model.Search;
import com.dietiestates.resourceserver.spec.SearchSpec;

public interface SearchMapper {

	SearchSpec toSpec(SearchRequest request);
	
	SearchResponse fromEntity(Search search);
}
