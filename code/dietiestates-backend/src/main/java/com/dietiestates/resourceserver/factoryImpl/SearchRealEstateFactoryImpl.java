package com.dietiestates.resourceserver.factoryImpl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.factory.SearchRealEstateFactory;
import com.dietiestates.resourceserver.model.RealEstate;
import com.dietiestates.resourceserver.model.Search;
import com.dietiestates.resourceserver.model.SearchRealEstate;

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
