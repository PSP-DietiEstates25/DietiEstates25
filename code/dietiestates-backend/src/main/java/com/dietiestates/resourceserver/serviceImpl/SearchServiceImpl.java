package com.dietiestates.resourceserver.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.dto.request.SearchRequest;
import com.dietiestates.resourceserver.dto.response.RealEstateResponse;
import com.dietiestates.resourceserver.factory.SearchFactory;
import com.dietiestates.resourceserver.finder.CadastralFilterFinder;
import com.dietiestates.resourceserver.finder.DetailFinder;
import com.dietiestates.resourceserver.finder.SearchFinder;
import com.dietiestates.resourceserver.finder.UserFinder;
import com.dietiestates.resourceserver.mapper.SearchMapper;
import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.model.Search;
import com.dietiestates.resourceserver.repository.SearchRepository;
import com.dietiestates.resourceserver.service.CadastralFilterService;
import com.dietiestates.resourceserver.service.SearchService;

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
	private final CadastralFilterFinder cadastralFilterFinder;
	private final DetailFinder detailFinder;
	
	private final SearchRealEstateServiceImpl searchRealEstateService;
	
	@Override
	public List<RealEstateResponse> createSearch(SearchRequest request) {
		
		var searchSpec = searchMapper.toSpec(request);
		
		var user = userFinder.getUserByEmail(searchSpec.getUserEmail());
	    var cadastralFilter = cadastralFilterFinder.getCadastralFilterById(searchSpec.getCadastralFilterId());
	    var detail = detailFinder.getDetailById(searchSpec.getDetailId());
	    
	    var search = searchFactory.createSearchFromSpec(searchSpec, user, cadastralFilter, detail);	    
	    var searchedRealEstates = this.getSearchedRealEstates(search);
	    
	    search = searchRepository.save(search);
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
