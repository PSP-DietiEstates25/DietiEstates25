package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.VisitNotFoundException;
import com.dietiestates.resource_server.finder.VisitFinder;
import com.dietiestates.resource_server.model.Visit;
import com.dietiestates.resource_server.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisitFinderDefaultImpl implements VisitFinder {

	private final VisitRepository visitRepository;

	@Override
	public Visit getVisitById(Long id)
			throws VisitNotFoundException {
		return visitRepository.findById(id)
				.orElseThrow(VisitNotFoundException::new);
	}
	
}
