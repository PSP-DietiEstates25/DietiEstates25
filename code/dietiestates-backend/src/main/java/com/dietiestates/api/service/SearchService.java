package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.SearchDto;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.exception.notfound.DetailsNotFoundException;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.DetailsRepository;
import com.dietiestates.api.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	
	private final SearchRepository searchRepository;
	private final DetailsRepository detailsRepository;
	
	public Search createSearch(SearchDto request) {
		var search = of(request);
		searchRepository.save(search);
		return search;
	}
	
	public Search of(SearchDto request) {
		var details = detailsRepository.findById(request.getDetailsId()).orElseThrow(DetailsNotFoundException::new);
		return Search.builder()
				.category(AdCategory.valueOf(request.getCategory()))
				.minimumPrice(request.getMinimumPrice())
				.maximumPrice(request.getMaximumPrice())
				.createdDate(LocalDateTime.now())
				.details(details)
				.build();
		
	}
}
