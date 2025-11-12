package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.model.SearchRealEstate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchRealEstateFinder {
    Page<SearchRealEstate> getSearchSearchRealEstates(Long searchId, Pageable pageable);
}
