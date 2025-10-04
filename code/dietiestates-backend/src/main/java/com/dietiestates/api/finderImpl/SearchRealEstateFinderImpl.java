package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.finder.SearchRealEstateFinder;
import com.dietiestates.api.repository.SearchRealEstateRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchRealEstateFinderImpl implements SearchRealEstateFinder {

	private final SearchRealEstateRepository searchRealEstateRepository;
}
