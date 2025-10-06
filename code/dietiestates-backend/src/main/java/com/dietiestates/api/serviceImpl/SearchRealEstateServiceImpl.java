package com.dietiestates.api.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.SearchRealEstate;
import com.dietiestates.api.repository.SearchRealEstateRepository;
import com.dietiestates.api.service.SearchRealEstateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchRealEstateServiceImpl implements SearchRealEstateService {

	private final SearchRealEstateRepository searchRealEstateRepository;
	
	@Override
	public void createSearchRealEstate(Search search, List<RealEstate> realEstates) {
		
		var searchRealEstates = new ArrayList<SearchRealEstate>();
		
		realEstates.forEach(realEstate -> {
			var searchRealEstate = of(search, realEstate);
			searchRealEstates.add(searchRealEstate);
		});
		
		var saved = searchRealEstateRepository.saveAll(searchRealEstates);
		
		saved.forEach(searchRealEstate -> {
			searchRealEstate.getRealEstate().addSearchRealEstate(searchRealEstate);
			searchRealEstate.getSearch().addSearchRealEstate(searchRealEstate);
		});
	}
	
	@Override
	public SearchRealEstate of(Search search, RealEstate realEstate) {
		return SearchRealEstate.searchRealEstateBuilder()
				.createdDate(LocalDateTime.now())
				.realEstate(realEstate)
				.search(search)
				.build();
	}
}