package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.UtilityNotFoundException;
import com.dietiestates.api.finder.UtilityFinder;
import com.dietiestates.api.model.Utility;
import com.dietiestates.api.repository.UtilityRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UtilityFinderImpl implements UtilityFinder{

	private final UtilityRepository utilityRepository;

	@Override
	public Utility getUtilityById(Long id)
			throws UtilityNotFoundException {
		return utilityRepository.findById(id)
				.orElseThrow(UtilityNotFoundException::new);
	}
	
}
