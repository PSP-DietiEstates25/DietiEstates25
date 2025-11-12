package com.dietiestates.resource_server.mapperdefaultimpl;

import com.dietiestates.resource_server.dto.request.SearchRequest;
import com.dietiestates.resource_server.dto.response.SearchResponse;
import com.dietiestates.resource_server.mapper.SearchMapper;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.spec.SearchSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchMapperDefaultImpl implements SearchMapper {
	
	@Override
	public SearchSpec toSpec(SearchRequest request) {
		return SearchSpec.builder()
				.category(request.getCategory())
				.cadastralFilterId(request.getCadastralFilterId())
				.detailId(request.getDetailId())
				.build();
	}
	
	@Override
	public SearchResponse fromEntity(Search search) {
		return SearchResponse.builder()
				.id(search.getId())
				.createdDate(search.getCreatedDate())
				.lastModifiedDate(search.getLastModifiedDate())
				.detailId(search.getDetail().getId())
				.cadastralFilterId(search.getCadastralFilter().getId())
				.userEmail(search.getUser().getEmail())
				.build();
	}

    @Override
    public Page<SearchResponse> createPagedSearchResponse(Page<Search> searches){
        return searches.map(this::fromEntity);
    }
}
