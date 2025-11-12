package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.VisitNotFoundException;
import com.dietiestates.resource_server.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitFinder {
	Visit getVisitById(Long id) throws VisitNotFoundException;
    Page<Visit> getRealEstateVisits(Long negotiationId, Pageable pageable);
}
