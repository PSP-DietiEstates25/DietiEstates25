package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.exception.notfound.VisitNotFoundException;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.Offer;
import com.dietiestates.resource_server.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VisitFinder {
	Visit getVisitById(Long id) throws VisitNotFoundException;
    Page<Visit> getRealEstateVisits(Long negotiationId, Pageable pageable);
    Page<Visit> getAllEstateAgentVisits(Long estateAgentId, String status, Pageable pageable);
    List<Visit> extractAllNegotiationsVisits(List<Negotiation> negotiations, String status);
}
