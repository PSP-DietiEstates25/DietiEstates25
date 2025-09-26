package com.dietiestates.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.DetailDto;
import com.dietiestates.api.dto.SearchDto;
import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.exception.notfound.UserNotFoundException;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.SearchRepository;
import com.dietiestates.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	
	private final SearchRepository searchRepository;
	private final UserRepository userRepository;
	private final RealEstateService realEstateService;
	private final SearchRealEstateService searchRealEstateService;
	
	private final GeographicalPositionService geographicalPositionService;
	private final UtilityService utilityService;
	private final DetailService detailService;
	private final CadastralFilterService cadastralFilterService;
	
	public List<RealEstate> createSearch(SearchDto request) {
		var search = of(request);
		var detailDto = DetailDto.builder()
				.searchId(search.getId())
				.build();	
		var detail = detailService.createDetail(detailDto);
		var geographicalPosition = geographicalPositionService.createGeographicalPosition(request.getGeographicalPositionDto(), detail.getId());
		var utility = utilityService.createUtility(request.getUtilityDto(), detail.getId());
		var cadastralFilter = cadastralFilterService.createCadastralFilter(request.getCadastralFilterDto(), search.getId());
		searchRepository.save(search);
		
		var searchRealEstates = realEstateService.getSearchRealEstates(search);
		searchRealEstateService.createSearchRealEstate(search, searchRealEstates);
		
		return searchRealEstates;
	}
	
	public Search of(SearchDto request) {
		
		var user = userRepository.findByEmail(request.getUserEmail())
				.orElseThrow(UserNotFoundException::new);
		
		return Search.builder()
				.createdDate(LocalDateTime.now())
				.category(AdCategory.valueOf(request.getCategory()))
				.size(request.getSize())
				.page(request.getPage() - 1)
				.user(user)
				.build();
		
	}
}
