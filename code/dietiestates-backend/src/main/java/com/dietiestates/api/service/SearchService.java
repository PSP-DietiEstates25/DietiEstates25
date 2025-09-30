package com.dietiestates.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.dto.request.SearchRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
import com.dietiestates.api.exception.notfound.UserNotFoundException;
import com.dietiestates.api.mapper.DetailMapper;
import com.dietiestates.api.mapper.RealEstateMapper;
import com.dietiestates.api.mapper.SearchMapper;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.SearchRepository;
import com.dietiestates.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	
	private final SearchRepository searchRepository;
	private final SearchMapper searchMapper;
	private final DetailMapper detailMapper;
	
	private final RealEstateMapper realEstateMapper;
	private final UserRepository userRepository;
	private final RealEstateService realEstateService;
	private final SearchRealEstateService searchRealEstateService;
	
	private final GeographicalPositionService geographicalPositionService;
	private final UtilityService utilityService;
	private final CadastralFilterService cadastralFilterService;
	
	public List<RealEstateResponse> createSearch(SearchRequest request) {
		
		var user = userRepository.findByEmail(request.getUserEmail())
				.orElseThrow(UserNotFoundException::new);
		
		var search = searchMapper.toEntity(request, user);
		
	    search = searchRepository.save(search);
	    
		var detailRequest = DetailRequest.builder()
				.build();
		
		var detail = detailMapper.toEntity(detailRequest, null, null);
		
		geographicalPositionService.createGeographicalPosition(request.getGeographicalPosition(), detail.getId());
		utilityService.createUtility(request.getUtility(), detail.getId());
		cadastralFilterService.createCadastralFilter(request.getCadastralFilter(), search.getId());
		
		var searchRealEstates = this.getSearchRealEstates(search);
		
		if(!searchRealEstates.isEmpty())
			searchRealEstateService.createSearchRealEstate(search, searchRealEstates);
		
		var response = new ArrayList<RealEstateResponse>();
		
		searchRealEstates.forEach(realEstate -> {
			var dto = realEstateMapper.fromEntity(realEstate);
			response.add(dto);
		});
		
		return response;
	}
	
	public List<RealEstate> getSearchRealEstates(Search search){
		
		var allRealEstates = realEstateService.getAllRealEstates();
		
		var geographicalPositionRealEstates = geographicalPositionService.getGeographicalPositionRealEstates(search.getDetail().getGeographicalPosition(), allRealEstates);
		var utilityRealEstates = utilityService.getUtilityRealEstates(search.getDetail().getUtility(), geographicalPositionRealEstates);
		var cadastralFilterRealEstates = cadastralFilterService.getCadastralFilterRealEstates(search.getCadastralFilter(), utilityRealEstates);
		
		return cadastralFilterRealEstates;
	}
}
