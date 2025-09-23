package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.SearchDto;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.exception.notfound.DetailsNotFoundException;
import com.dietiestates.api.exception.notfound.UserNotFoundException;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.DetailsRepository;
import com.dietiestates.api.repository.SearchRepository;
import com.dietiestates.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	
	private final SearchRepository searchRepository;
	private final DetailsRepository detailsRepository;
	private final UserRepository userRepository;
	
	public void createSearch(SearchDto request) {
		var search = of(request);
		searchRepository.save(search);
	}
	
	public Search of(SearchDto request) {
		
		var user = userRepository.findByEmail(request.getUserEmail())
				.orElseThrow(UserNotFoundException::new);
		
		var details = detailsRepository.findById(request.getDetailsId())
				.orElseThrow(DetailsNotFoundException::new);
		
		return Search.builder()
				.category(AdCategory.valueOf(request.getCategory()))
				.minimumPrice(request.getMinimumPrice())
				.maximumPrice(request.getMaximumPrice())
				.createdDate(LocalDateTime.now())
				.detail(details)
				.user(user)
				.build();
		
	}
}
