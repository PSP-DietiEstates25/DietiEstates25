package com.dietiestates.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.dto.request.SearchRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
import com.dietiestates.api.exception.notfound.SearchNotFoundException;
import com.dietiestates.api.exception.notfound.UserNotFoundException;
import com.dietiestates.api.mapper.RealEstateMapper;
import com.dietiestates.api.mapper.SearchMapper;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.User;
import com.dietiestates.api.repository.SearchRepository;
import com.dietiestates.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	
	private final SearchRepository searchRepository;
	private final SearchMapper searchMapper;
	
	private final RealEstateMapper realEstateMapper;
	private final UserRepository userRepository;
	private final RealEstateService realEstateService;
	private final DetailService detailService;
	private final SearchRealEstateService searchRealEstateService;
	
	private final GeographicalPositionService geographicalPositionService;
	private final UtilityService utilityService;
	private final CadastralFilterService cadastralFilterService;
	
	public List<RealEstateResponse> createSearch(SearchRequest request) {
		
		var user = this.getSearchUser(request);		
		var search = searchMapper.toEntity(request, user);
	    search = searchRepository.save(search);
		var detailRequest = this.createDetailRequest(search);
		var detail = detailService.createDetail(detailRequest);
		
		this.setSearchFilters(request, search, detail);
		var searchRealEstates = this.getSearchRealEstates(search);
		this.createSearchRealEstate(search, searchRealEstates);
		
		return this.createSearchResponse(searchRealEstates);
	}
	
	public DetailRequest createDetailRequest(Search search) {
		return DetailRequest.builder()
				.searchId(search.getId())
				.build();
	}
	
	public List<RealEstateResponse> createSearchResponse(List<RealEstate> searchRealEstates) {
		var response = new ArrayList<RealEstateResponse>();
		
		searchRealEstates.forEach(realEstate -> {
			var dto = realEstateMapper.fromEntity(realEstate);
			response.add(dto);
		});
		
		return response;
	}
	
	public void createSearchRealEstate(Search search, List<RealEstate> searchRealEstates) {
		if(!searchRealEstates.isEmpty())
			searchRealEstateService.createSearchRealEstate(search, searchRealEstates);
	}
	
	public Search getSearchById(Long searchId) {
		return searchRepository.findById(searchId)
				.orElseThrow(SearchNotFoundException::new);
	}
	
	public void setSearchFilters(SearchRequest request, Search search, Detail detail) {
		geographicalPositionService.createGeographicalPosition(request.getGeographicalPosition(), detail.getId());
		utilityService.createUtility(request.getUtility(), detail.getId());
		cadastralFilterService.createCadastralFilter(request.getCadastralFilter(), search.getId());
	}
	
	public User getSearchUser(SearchRequest request) {
		return userRepository.findByEmail(request.getUserEmail())
				.orElseThrow(UserNotFoundException::new);
	}
	
	public List<RealEstate> getSearchRealEstates(Search search){
		
		var allRealEstates = realEstateService.getAllRealEstates();
		
		var geographicalPositionRealEstates = geographicalPositionService.getGeographicalPositionRealEstates(search.getDetail().getGeographicalPosition(), allRealEstates);
		var utilityRealEstates = utilityService.getUtilityRealEstates(search.getDetail().getUtility(), geographicalPositionRealEstates);
		var cadastralFilterRealEstates = cadastralFilterService.getCadastralFilterRealEstates(search.getCadastralFilter(), utilityRealEstates);
		
		return cadastralFilterRealEstates;
	}
}
