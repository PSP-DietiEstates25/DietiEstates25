package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.finder.SearchRealEstateFinder;
import com.dietiestates.resource_server.repository.SearchRealEstateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchRealEstateFinderDefaultImpl implements SearchRealEstateFinder {

	private final SearchRealEstateRepository searchRealEstateRepository;
}
