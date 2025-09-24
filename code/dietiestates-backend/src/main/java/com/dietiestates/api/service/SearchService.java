package com.dietiestates.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.SearchDto;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.exception.notfound.DetailsNotFoundException;
import com.dietiestates.api.exception.notfound.UserNotFoundException;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.DetailsRepository;
import com.dietiestates.api.repository.RealEstateRepository;
import com.dietiestates.api.repository.SearchRealEstateRepository;
import com.dietiestates.api.repository.SearchRepository;
import com.dietiestates.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	
	private final SearchRepository searchRepository;
	private final RealEstateRepository realEstateRepository;
	private final SearchRealEstateRepository searchRealEstateRepository;
	private final UserRepository userRepository;
	
	private final DetailsRepository detailsRepository;
	//private final GeographicalPositionRepository geographicalPositionRepository;
	
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
				.createdDate(LocalDateTime.now())
				.category(AdCategory.valueOf(request.getCategory()))
				.size(request.getSize())
				.page(request.getPage() - 1)
				.detail(details)
				.user(user)
				.build();
		
	}
}
