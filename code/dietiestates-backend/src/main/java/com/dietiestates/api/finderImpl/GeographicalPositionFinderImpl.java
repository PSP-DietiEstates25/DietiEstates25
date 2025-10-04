package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.GeographicalPositionNotFoundException;
import com.dietiestates.api.finder.GeographicalPositionFinder;
import com.dietiestates.api.model.GeographicalPosition;
import com.dietiestates.api.repository.GeographicalPositionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GeographicalPositionFinderImpl implements GeographicalPositionFinder {
	
	private final GeographicalPositionRepository geographicalPositionRepository;
	
	@Override
	public GeographicalPosition getGeographicalPositionById(Long id)
			throws GeographicalPositionNotFoundException {
		return geographicalPositionRepository.findById(id)
				.orElseThrow(GeographicalPositionNotFoundException::new);
	}

}
