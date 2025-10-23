package com.dietiestates.resourceserver.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.GeographicalPositionNotFoundException;
import com.dietiestates.resourceserver.finder.GeographicalPositionFinder;
import com.dietiestates.resourceserver.model.GeographicalPosition;
import com.dietiestates.resourceserver.repository.GeographicalPositionRepository;

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
