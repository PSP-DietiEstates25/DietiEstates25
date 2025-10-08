package com.dietiestates.api.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.SearchRealEstate;
import com.dietiestates.api.service.SearchRealEstateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchRealEstateServiceImpl implements SearchRealEstateService {
	
	@Override
	public void createSearchRealEstate(Search search, List<RealEstate> realEstates) {
		
		var searchRealEstates = new ArrayList<SearchRealEstate>();
		
		realEstates.forEach(realEstate -> {
			var searchRealEstate = of(search, realEstate);
			searchRealEstates.add(searchRealEstate);
		});
		
	}
	
	@Override
	public SearchRealEstate of(Search search, RealEstate realEstate) {
		return SearchRealEstate.builder()
				.realEstate(realEstate)
				.search(search)
				.build();
	}
}