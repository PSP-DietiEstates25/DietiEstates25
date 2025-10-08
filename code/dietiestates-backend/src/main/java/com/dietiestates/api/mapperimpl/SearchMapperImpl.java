package com.dietiestates.api.mapperimpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.SearchRequest;
import com.dietiestates.api.dto.response.SearchResponse;
import com.dietiestates.api.mapper.SearchMapper;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.spec.SearchSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchMapperImpl implements SearchMapper {
	
	@Override
	public SearchSpec toSpec(SearchRequest request) {
		return SearchSpec.builder()
				.category(request.getCategory())
				.size(request.getSize())
				.page(request.getPage())
				.userEmail(request.getUserEmail())
				.detailId(request.getDetailId())
				.cadastralFilterId(request.getCadastralFilterId())
				.build();
	}
	
	@Override
	public SearchResponse fromEntity(Search search) {
		return SearchResponse.builder()
				.id(search.getId())
				.createdDate(search.getCreatedDate())
				.lastModifiedDate(search.getLastModifiedDate())
				.size(search.getSize())
				.page(search.getPage())
				.detailId(search.getDetail().getId())
				.cadastralFilterId(search.getCadastralFilter().getId())
				.userEmail(search.getUser().getSecurityAccountDecorator().getAccountEmail())
				.build();
	}
}
