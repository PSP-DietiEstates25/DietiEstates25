package com.dietiestates.resourceserver.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.model.Search;
import com.dietiestates.resourceserver.model.SearchRealEstate;
import com.dietiestates.resourceserver.service.SearchRealEstateService;

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