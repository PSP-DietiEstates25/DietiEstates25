package com.dietiestates.api.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.SearchRequest;
import com.dietiestates.api.dto.response.SearchResponse;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.User;
import com.dietiestates.api.spec.SearchSpec;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchMapper {

	private final CadastralFilterMapper cadastralFilterMapper;
	
	public Search toEntity(SearchRequest request, User user) {
		return Search.builder()
				.createdDate(LocalDateTime.now())
				.category(AdCategory.valueOf(request.getCategory()))
				.size(request.getSize())
				.page(request.getPage() - 1)
				.user(user)
				.build();
	}
	
	public SearchSpec toSpec(SearchRequest request) {
		return SearchSpec.builder()
				.category(request.getCategory())
				.size(request.getSize())
				.page(request.getPage())
				.userEmail(request.getUserEmail())
				.detailId(request.getDetailId())
				.cadastralFilterSpec(cadastralFilterMapper.toSpec(request.getCadastralFilter()))
				.build();
	}
	
	public SearchResponse fromEntity(Search search) {
		return SearchResponse.builder()
				.id(search.getId())
				.createdDate(search.getCreatedDate())
				.lastModifiedDate(search.getLastModifiedDate())
				.size(search.getSize())
				.page(search.getPage())
				.detailId(search.getDetail().getId())
				.cadastralFilterId(search.getCadastralFilter().getId())
				.userEmail(search.getUser().getEmail())
				.build();
	}
}
