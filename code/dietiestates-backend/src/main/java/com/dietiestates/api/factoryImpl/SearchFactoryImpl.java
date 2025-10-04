package com.dietiestates.api.factoryImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.dto.request.SearchRequest;
import com.dietiestates.api.factory.SearchFactory;
import com.dietiestates.api.model.Search;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchFactoryImpl implements SearchFactory {

	@Override
	public Search createSearch(SearchRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
