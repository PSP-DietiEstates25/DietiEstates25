package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.SearchNotFoundException;
import com.dietiestates.api.finder.SearchFinder;
import com.dietiestates.api.model.Search;
import com.dietiestates.api.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchFinderImpl implements SearchFinder {

	private final SearchRepository searchRepository;

	@Override
	public Search getSearchById(Long id)
			throws SearchNotFoundException {
		return searchRepository.findById(id)
				.orElseThrow(SearchNotFoundException::new);
	}
	
}
