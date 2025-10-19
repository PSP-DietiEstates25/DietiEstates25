package com.dietiestates.resourceserver.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.DetailNotFoundException;
import com.dietiestates.resourceserver.finder.DetailFinder;
import com.dietiestates.resourceserver.model.Detail;
import com.dietiestates.resourceserver.repository.DetailRepository;

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
