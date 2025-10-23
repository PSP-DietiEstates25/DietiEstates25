package com.dietiestates.resourceserver.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.finder.SearchRealEstateFinder;
import com.dietiestates.resourceserver.repository.SearchRealEstateRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchRealEstateFinderImpl implements SearchRealEstateFinder {

	private final SearchRealEstateRepository searchRealEstateRepository;
}
