package com.dietiestates.api.finderImpl;

import org.springframework.stereotype.Component;

import com.dietiestates.api.exception.notfound.VisitNotFoundException;
import com.dietiestates.api.finder.VisitFinder;
import com.dietiestates.api.model.Visit;
import com.dietiestates.api.repository.VisitRepository;

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
