package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.finder.SearchRealEstateFinder;
import com.dietiestates.resource_server.model.SearchRealEstate;
import com.dietiestates.resource_server.repository.SearchRealEstateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchRealEstateFinderDefaultImpl implements SearchRealEstateFinder {

	private final SearchRealEstateRepository searchRealEstateRepository;

    @Override
    public Page<SearchRealEstate> getSearchSearchRealEstates(Long searchId, Pageable pageable) {
        return searchRealEstateRepository.findBySearchId(searchId, pageable);
    }
}
