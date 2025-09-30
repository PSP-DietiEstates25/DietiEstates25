package com.dietiestates.api.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.SearchRequest;
import com.dietiestates.api.dto.response.SearchResponse;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.User;

@Component
public class SearchMapper {

	public Search toEntity(SearchRequest request, User user) {
		return Search.builder()
				.createdDate(LocalDateTime.now())
				.category(AdCategory.valueOf(request.getCategory()))
				.size(request.getSize())
				.page(request.getPage() - 1)
				.user(user)
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
