package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.SearchRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.factory.SearchFactory;
import com.dietiestates.resource_server.finder.CadastralFilterFinder;
import com.dietiestates.resource_server.finder.DetailFinder;
import com.dietiestates.resource_server.finder.SearchFinder;
import com.dietiestates.resource_server.finder.UserFinder;
import com.dietiestates.resource_server.mapper.RealEstateMapper;
import com.dietiestates.resource_server.mapper.SearchMapper;
import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.repository.SearchRepository;
import com.dietiestates.resource_server.service.CadastralFilterService;
import com.dietiestates.resource_server.service.RealEstateService;
import com.dietiestates.resource_server.service.SearchRealEstateService;
import com.dietiestates.resource_server.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceDefaultImpl implements SearchService {

    private final RealEstateService realEstateService;
	private final CadastralFilterService cadastralFilterService;
	
	private final SearchRepository searchRepository;
	private final SearchFactory searchFactory;
	private final SearchFinder searchFinder;
	private final SearchMapper searchMapper;
	
	private final UserFinder userFinder;
	private final CadastralFilterFinder cadastralFilterFinder;
	private final DetailFinder detailFinder;
    private final RealEstateMapper realEstateMapper;
	
	private final SearchRealEstateService searchRealEstateService;
	
	@Override
	public List<RealEstateResponse> createSearch(SearchRequest request, String userEmail) {
		
		var searchSpec = searchMapper.toSpec(request);
		
		var user = userFinder.getUserByEmail(userEmail);
	    var cadastralFilter = cadastralFilterFinder.getCadastralFilterById(searchSpec.getCadastralFilterId());
	    var detail = detailFinder.getDetailById(searchSpec.getDetailId());
	    
	    var search = searchFactory.createSearchFromSpec(searchSpec, user, cadastralFilter, detail);	    
	    var searchedRealEstates = searchRealEstateService.getSearchedRealEstates(search);
	    
	    search = searchRepository.save(search);
		return realEstateMapper.createRealEstatesResponse(searchedRealEstates);
	}
}
