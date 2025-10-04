package com.dietiestates.api.factoryImpl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.api.factory.SearchRealEstateFactory;
import com.dietiestates.api.model.RealEstate;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.model.SearchRealEstate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchRealEstateFactoryImpl implements SearchRealEstateFactory {

	@Override
	public SearchRealEstate createSearchRealEstate(Search search, List<RealEstate> realEstates) {
		// TODO Auto-generated method stub
		return null;
	}

}
