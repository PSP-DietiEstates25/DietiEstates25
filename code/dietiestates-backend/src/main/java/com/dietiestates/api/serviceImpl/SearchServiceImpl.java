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
import com.dietiestates.api.service.CadastralFilterService;
import com.dietiestates.api.service.SearchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
	
	private final RealEstateServiceImpl realEstateService;
	private final CadastralFilterService cadastralFilterService;
	
	private final SearchRepository searchRepository;
	private final SearchFactory searchFactory;
	private final SearchFinder searchFinder;
	private final SearchMapper searchMapper;
	
	private final UserFinder userFinder;
	private final DetailFinder detailFinder;
	
	private final SearchRealEstateServiceImpl searchRealEstateService;
	
	@Override
	public List<RealEstateResponse> createSearch(SearchRequest request) {
		
		var searchSpec = searchMapper.toSpec(request);
		
		var user = userFinder.getUserByEmail(searchSpec.getUserEmail());

		var search = searchFactory.createSearchFromSpec(searchSpec, user);
	    search = searchRepository.save(search);
	    
	    var detail = detailFinder.getDetailById(searchSpec.getDetailId());
	    search.setDetail(detail);
	    
	    search = searchRepository.save(search);
	    
	    cadastralFilterService.createCadastralFilter(request.getCadastralFilter(), search.getId());
		
	    var searchedRealEstates = this.getSearchedRealEstates(search);
		return realEstateService.createRealEstatesResponse(searchedRealEstates);
	}
	
	@Override
	public void createSearchRealEstate(Search search, List<RealEstate> searchRealEstates) {
		if(!searchRealEstates.isEmpty())
			searchRealEstateService.createSearchRealEstate(search, searchRealEstates);
	}
	
	@Override
	public List<RealEstate> getSearchedRealEstates(Search search) {
		
		var searchedRealEstates = realEstateService.getRealEstatesBySearchFilter(search);
		
		this.createSearchRealEstate(search, searchedRealEstates);
		return searchedRealEstates;
	}

}
