package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.GeographicalPositionNotFoundException;
import com.dietiestates.resource_server.finder.GeographicalPositionFinder;
import com.dietiestates.resource_server.model.GeographicalPosition;
import com.dietiestates.resource_server.repository.GeographicalPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeographicalPositionFinderDefaultImpl implements GeographicalPositionFinder {
	
	private final GeographicalPositionRepository geographicalPositionRepository;
	
	@Override
	public GeographicalPosition getGeographicalPositionById(Long id) throws GeographicalPositionNotFoundException {
		return geographicalPositionRepository.findById(id)
				.orElseThrow(GeographicalPositionNotFoundException::new);
	}

}
