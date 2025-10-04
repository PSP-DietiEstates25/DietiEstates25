package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.DetailNotFoundException;
import com.dietiestates.api.finder.DetailFinder;
import com.dietiestates.api.model.Detail;
import com.dietiestates.api.repository.DetailRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DetailFinderImpl implements DetailFinder {

	private final DetailRepository detailRepository;

	@Override
	public Detail getDetailById(Long id)
			throws DetailNotFoundException {
		return detailRepository.findById(id)
				.orElseThrow(DetailNotFoundException::new);
	}
	
}
