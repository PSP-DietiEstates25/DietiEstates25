package com.dietiestates.resource_server.mapper;

import com.dietiestates.resource_server.dto.request.SearchRequest;
import com.dietiestates.resource_server.dto.response.SearchResponse;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.spec.SearchSpec;

public interface SearchMapper {
	SearchSpec toSpec(SearchRequest request);
	SearchResponse fromEntity(Search search);
}
