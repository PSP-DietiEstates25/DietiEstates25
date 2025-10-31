package com.dietiestates.api.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.dietiestates.api.model.Visit;

public interface VisitRepository extends
	CrudRepository<Visit, Long>,
	PagingAndSortingRepository<Visit, Long> {

	List<Visit> findByUser(String userEmail, Pageable pageable);
	List<Visit> findByRealEstate(Long realEstateId, Pageable pageable);
  List<Visit> findByRealEstateId(Long realEstateId, Pageable pageable);
	
	/*

  Page<Visit> findByUser_EmailOrderByStartAtDesc(String email, Pageable pageable);

  Page<Visit> findByEstateAgent_EmailOrderByStartAtDesc(String email, Pageable pageable);

  Page<Visit> findByEstateAgent_EmailAndStatusOrderByStartAtDesc(String email, VisitStatus status, Pageable pageable);

  @Query("""
          SELECT (COUNT(v) > 0)
          FROM Visit v
          WHERE v.estateAgent.email = :agentEmail
            AND v.status IN :statuses
            AND v.startAt = :start
      """)
  boolean existsAgentSlot(
      @Param("agentEmail") String agentEmail,
      @Param("start") Instant start,
      @Param("statuses") List<VisitStatus> statuses);
	 */
}
