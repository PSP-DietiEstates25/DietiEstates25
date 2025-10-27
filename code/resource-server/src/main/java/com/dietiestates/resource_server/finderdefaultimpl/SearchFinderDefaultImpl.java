package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.SearchNotFoundException;
import com.dietiestates.resource_server.finder.SearchFinder;
import com.dietiestates.resource_server.model.Search;
import com.dietiestates.resource_server.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchFinderDefaultImpl implements SearchFinder {

	private final SearchRepository searchRepository;

	@Override
	public Search getSearchById(Long id)
			throws SearchNotFoundException {
		return searchRepository.findById(id)
				.orElseThrow(SearchNotFoundException::new);
	}
	
}
