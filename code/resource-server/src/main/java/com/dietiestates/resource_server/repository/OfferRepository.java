package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.enums.ProposalStatus;
import com.dietiestates.resource_server.model.Negotiation;
import com.dietiestates.resource_server.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends CrudRepository<Offer, Long>, PagingAndSortingRepository<Offer, Long>{
    boolean existsById(Long id);
    boolean existsByIdAndNegotiationId(Long id, Long negotiationId);
    Page<Offer> findByNegotiationId(Long negotiationId, Pageable pageable);
    Page<Offer> findByNegotiationIdAndProposalStatus(Long negotiationId, ProposalStatus proposalStatus, Pageable pageable);
}
