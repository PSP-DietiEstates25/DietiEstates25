package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.VisitNotFoundException;
import com.dietiestates.resource_server.finder.VisitFinder;
import com.dietiestates.resource_server.model.Visit;
import com.dietiestates.resource_server.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisitFinderDefaultImpl implements VisitFinder {

	private final VisitRepository visitRepository;

	@Override
	public Visit getVisitById(Long id) throws VisitNotFoundException {
		return visitRepository.findById(id)
				.orElseThrow(VisitNotFoundException::new);
	}

	@Override
	public Page<Visit> getRealEstateVisits(Long realEstateId, Pageable pageable) {
		return visitRepository.findByNegotiation_RealEstate_Id(realEstateId, pageable);
	}

}
