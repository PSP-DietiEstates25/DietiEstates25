package com.dietiestates.api.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.dto.request.SearchRequest;
import com.dietiestates.api.dto.response.RealEstateResponse;
import com.dietiestates.api.factory.SearchFactory;
import com.dietiestates.api.finder.DetailFinder;
import com.dietiestates.api.finder.SearchFinder;
import com.dietiestates.api.finder.UserFinder;
import com.dietiestates.api.mapper.SearchMapper;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	
	private final RealEstateService realEstateService;
	private final CadastralFilterService cadastralFilterService;
	
	private final SearchRepository searchRepository;
	private final SearchFactory searchFactory;
	private final SearchFinder searchFinder;
	private final SearchMapper searchMapper;
	
	private final UserFinder userFinder;
	private final DetailFinder detailFinder;
	
	
	private final SearchRealEstateService searchRealEstateService;
	
	private final GeographicalPositionService geographicalPositionService;
	private final UtilityService utilityService;
	
	public List<RealEstateResponse> createSearch(SearchRequest request) {
		
		var searchSpec = searchMapper.toSpec(request);

		System.out.print("=============================================");
		System.out.println(searchSpec.toString());
		System.out.print("=============================================");
		
		var user = userFinder.getUserByEmail(searchSpec.getUserEmail());

		var search = searchFactory.createSearchFromSpec(searchSpec, user);
	    search = searchRepository.save(search);
	    
	    //detailService.createDetail(createDetailRequest(search));
	    var detail = detailFinder.getDetailById(searchSpec.getDetailId());
	    search.setDetail(detail);
	    
	    search = searchRepository.save(search);
	    
	    cadastralFilterService.createCadastralFilter(request.getCadastralFilter(), search.getId());
	    /*
	    this.setSearchDetail(search, request.getDetailId());
	    this.setSearchCadastralFilter(search, request.getCadastralFilter());
		*/
		
	    var searchedRealEstates = this.getSearchedRealEstates(search);
		return realEstateService.createRealEstatesResponse(searchedRealEstates);
	}
	
	/*
	public DetailRequest createDetailRequest(Search search) {
		return DetailRequest.builder()
				.searchId(search.getId())
				.build();
	}
	*/
	
	public void createSearchRealEstate(Search search, List<RealEstate> searchRealEstates) {
		if(!searchRealEstates.isEmpty())
			searchRealEstateService.createSearchRealEstate(search, searchRealEstates);
	}
	
	//realEstateFinder - getRealEstatesBySearch
	public List<RealEstate> getSearchedRealEstates(Search search){
		
		var searchedRealEstates = realEstateService.getRealEstatesBySearchFilter(search);
		
		this.createSearchRealEstate(search, searchedRealEstates);
		return searchedRealEstates;
	}
		
	/*
	public void setSearchCadastralFilters(Search search, CadastralFilterRequest cadastralFilterRequest) {
		var cadastralFilter = cadastralFilterService.createCadastralFilter(cadastralFilterRequest, search.getId());
		cadastralFilter.setSearch(search);
		
	}
	
	public void setSearchDetail(Search search, Long detailId) {
		var detail = detailService.getDetailById(detailId);
		detail.setSearch(search);
	}
	*/
}
