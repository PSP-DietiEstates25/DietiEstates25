package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.UtilityNotFoundException;
import com.dietiestates.resource_server.finder.UtilityFinder;
import com.dietiestates.resource_server.model.Utility;
import com.dietiestates.resource_server.repository.UtilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UtilityFinderDefaultImpl implements UtilityFinder{

	private final UtilityRepository utilityRepository;

	@Override
	public Utility getUtilityById(Long id) throws UtilityNotFoundException {
		return utilityRepository.findById(id)
				.orElseThrow(UtilityNotFoundException::new);
	}
}
