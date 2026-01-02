package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.dto.request.SearchRequest;
import com.dietiestates.resource_server.dto.response.RealEstateResponse;
import com.dietiestates.resource_server.dto.response.SearchResponse;
import com.dietiestates.resource_server.factory.SearchFactory;
import com.dietiestates.resource_server.finder.CadastralFilterFinder;
import com.dietiestates.resource_server.finder.DetailFinder;
import com.dietiestates.resource_server.finder.SearchFinder;
import com.dietiestates.resource_server.finder.UserFinder;
import com.dietiestates.resource_server.mapper.RealEstateMapper;
import com.dietiestates.resource_server.mapper.SearchMapper;
import com.dietiestates.resource_server.repository.SearchRepository;
import com.dietiestates.resource_server.service.*;
import com.dietiestates.resource_server.verifier.SearchVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceDefaultImpl implements SearchService {
	
	private final SearchRepository searchRepository;
	private final SearchFactory searchFactory;
	private final SearchFinder searchFinder;
    private final SearchVerifier searchVerifier;
	private final SearchMapper searchMapper;
	
	private final UserFinder userFinder;
	private final CadastralFilterFinder cadastralFilterFinder;
	private final DetailFinder detailFinder;

    private final RealEstateMapper realEstateMapper;

    private final SearchRealEstateMatchingService searchRealEstateMatchingService;

	@Override
	public List<RealEstateResponse> createSearch(SearchRequest request, String userEmail) {
		
		var searchSpec = searchMapper.toSpec(request);
		
		var user = userFinder.getUserByEmail(userEmail);
	    var cadastralFilter = cadastralFilterFinder.getCadastralFilterById(searchSpec.getCadastralFilterId());
	    var detail = detailFinder.getDetailById(searchSpec.getDetailId());
	    
	    var search = searchFactory.createSearchFromSpec(searchSpec, user, cadastralFilter, detail);
        searchRepository.save(search);

	    var searchedRealEstates = searchRealEstateMatchingService.getRealEstatesBySearchFilter(search);
		return realEstateMapper.createRealEstatesResponse(searchedRealEstates);
	}

    @Override
    public Page<SearchResponse> getUserSearches(String userEmail, Integer page, Integer size) {

        String createdDate = "createdDate";
        Pageable pageable = PageRequest.of(page, size, Sort.by(createdDate).descending());
        var user = userFinder.getUserByEmail(userEmail);
        var userSearches = searchFinder.getUserSearches(user.getId(), pageable);

        return searchMapper.createPagedSearchResponse(userSearches);
    }

    @Override
    public List<RealEstateResponse> runSavedSearch(Long searchId, String userEmail) {
        searchVerifier.checkSearchOwnedByUser(searchId, userEmail);

        var search = searchFinder.getSearchById(searchId);
        var currentResults = searchRealEstateMatchingService.getRealEstatesBySearchFilter(search);

        return realEstateMapper.createRealEstatesResponse(currentResults);
    }
}
