package com.dietiestates.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.CadastralFilterRequest;
import com.dietiestates.api.dto.request.DetailRequest;
import com.dietiestates.api.dto.request.SearchRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
import com.dietiestates.api.exception.notfound.SearchNotFoundException;
import com.dietiestates.api.mapper.SearchMapper;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	
	private final SearchRepository searchRepository;
	private final SearchMapper searchMapper;
	
	private final UserService userService;
	private final DetailService detailService;
	
	private final RealEstateService realEstateService;
	private final SearchRealEstateService searchRealEstateService;
	
	private final GeographicalPositionService geographicalPositionService;
	private final UtilityService utilityService;
	private final CadastralFilterService cadastralFilterService;
	
	public List<RealEstateResponse> createSearch(SearchRequest request) {
		
		var user = userService.getUserByEmail(request.getUserEmail());

		var search = searchMapper.toEntity(request, user);
	    search = searchRepository.save(search);
	    
	    this.setDetail(search, request.getDetailId());
	    this.setCadastralFilters(search, request.getCadastralFilter());
		
	    var searchedRealEstates = this.getSearchedRealEstates(search);
	    
		return realEstateService.createRealEstatesResponse(searchedRealEstates);
	}
	
	public DetailRequest createDetailRequest(Search search) {
		return DetailRequest.builder()
				.searchId(search.getId())
				.build();
	}
	
	public void createSearchRealEstate(Search search, List<RealEstate> searchRealEstates) {
		if(!searchRealEstates.isEmpty())
			searchRealEstateService.createSearchRealEstate(search, searchRealEstates);
	}
	
	//realEstateFinder - getRealEstatesBySearch
	public List<RealEstate> getSearchedRealEstates(Search search){
		var searchedRealEstates = this.getRealEstates(search);
		this.createSearchRealEstate(search, searchedRealEstates);
		return searchedRealEstates;
	}
	
	//realEstateFinder
	//getRealEstatesBySearchFilters
	
	//getRealEstatesByGeographicalPosition
	//getRealEstatesByUtility
	//getRealEstatesByCadastralFilter
	public List<RealEstate> getRealEstates(Search search){
		
		var allRealEstates = realEstateService.getAllRealEstates();
		
		var geographicalPositionRealEstates = geographicalPositionService.getGeographicalPositionRealEstates(search.getDetail().getGeographicalPosition(), allRealEstates);
		var utilityRealEstates = utilityService.getUtilityRealEstates(search.getDetail().getUtility(), geographicalPositionRealEstates);
		var cadastralFilterRealEstates = cadastralFilterService.getCadastralFilterRealEstates(search.getCadastralFilter(), utilityRealEstates);
		
		return cadastralFilterRealEstates;
	}
	
	public Search getSearchById(Long searchId) {
		return searchRepository.findById(searchId)
				.orElseThrow(SearchNotFoundException::new);
	}
	
	public void setCadastralFilters(Search search, CadastralFilterRequest cadastralFilterRequest) {
		var cadastralFilter = cadastralFilterService.createCadastralFilter(cadastralFilterRequest, search.getId());
		cadastralFilter.setSearch(search);
		
	}
	
	public void setDetail(Search search, Long detailId) {
		var detail = detailService.getDetailById(detailId);
		detail.setSearch(search);
	}
	
}
