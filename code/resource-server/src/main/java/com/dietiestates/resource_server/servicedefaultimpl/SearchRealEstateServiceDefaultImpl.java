package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.model.RealEstate;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.model.SearchRealEstate;
import com.dietiestates.resource_server.service.SearchRealEstateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchRealEstateServiceDefaultImpl implements SearchRealEstateService {
	
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