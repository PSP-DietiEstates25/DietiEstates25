package com.dietiestates.resourceserver.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.SearchNotFoundException;
import com.dietiestates.resourceserver.finder.SearchFinder;
import com.dietiestates.resourceserver.model.Search;
import com.dietiestates.resourceserver.repository.SearchRepository;

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
