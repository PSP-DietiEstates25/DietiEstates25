package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.SearchNotFoundException;
import com.dietiestates.resource_server.model.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchFinder {
	Search getSearchById(Long id) throws SearchNotFoundException;
    List<Search> getAllSearches();
    Page<Search> getUserSearches(Long userId, Pageable pageable);
}
