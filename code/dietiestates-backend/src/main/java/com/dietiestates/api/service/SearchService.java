package com.dietiestates.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.dto.request.SearchRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
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
	
	public List<RealEstateResponse> createSearch(SearchRequest request) {
		var search = of(request);
	    search = searchRepository.save(search);
		var detailDto = DetailRequest.builder()
				.searchId(search.getId())
				.build();
		var detail = detailService.createDetail(detailDto);
		
		geographicalPositionService.createGeographicalPosition(request.getGeographicalPosition(), detail.getId());
		utilityService.createUtility(request.getUtility(), detail.getId());
		cadastralFilterService.createCadastralFilter(request.getCadastralFilter(), search.getId());
		
		var searchRealEstates = this.getSearchRealEstates(search);
		
		if(!searchRealEstates.isEmpty())
			searchRealEstateService.createSearchRealEstate(search, searchRealEstates);
		
		var response = new ArrayList<RealEstateResponse>();
		
		searchRealEstates.forEach(realEstate -> {
			var dto = RealEstateResponse.builder()
					.createdDate(realEstate.getCreatedDate())
					.lastModifiedDate(realEstate.getLastModifiedDate())
					.id(realEstate.getId())
					.category(realEstate.getCategory().toString())
					.images(realEstate.getImages())
					.description(realEstate.getDescription())
					.estateAgentEmail(realEstate.getEstateAgent().getEmail())
					.detailId(realEstate.getDetail().getId())
					.cadastralDataId(realEstate.getCadastralData().getId())
					.build();
			
			response.add(dto);
		});
		
		return response;
	}
	
	public Search of(SearchRequest request) {
		
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
	
	public List<RealEstate> getSearchRealEstates(Search search){
		
		var allRealEstates = realEstateService.getAllRealEstates();
		
		var geographicalPositionRealEstates = geographicalPositionService.getGeographicalPositionRealEstates(search.getDetail().getGeographicalPosition(), allRealEstates);
		var utilityRealEstates = utilityService.getUtilityRealEstates(search.getDetail().getUtility(), geographicalPositionRealEstates);
		var cadastralFilterRealEstates = cadastralFilterService.getCadastralFilterRealEstates(search.getCadastralFilter(), utilityRealEstates);
		
		return cadastralFilterRealEstates;
	}
}
