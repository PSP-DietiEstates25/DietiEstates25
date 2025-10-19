package com.dietiestates.resourceserver.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.UtilityNotFoundException;
import com.dietiestates.resourceserver.finder.UtilityFinder;
import com.dietiestates.resourceserver.model.Utility;
import com.dietiestates.resourceserver.repository.UtilityRepository;

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
