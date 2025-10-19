package com.dietiestates.resourceserver.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.resourceserver.exception.notfound.VisitNotFoundException;
import com.dietiestates.resourceserver.finder.VisitFinder;
import com.dietiestates.resourceserver.model.Visit;
import com.dietiestates.resourceserver.repository.VisitRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VisitFinderImpl implements VisitFinder {

	private final VisitRepository visitRepository;

	@Override
	public Visit getVisitById(Long id)
			throws VisitNotFoundException {
		return visitRepository.findById(id)
				.orElseThrow(VisitNotFoundException::new);
	}
	
}
