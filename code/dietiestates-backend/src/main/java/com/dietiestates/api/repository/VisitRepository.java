package com.dietiestates.api.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

//import com.dietiestates.api.enums.VisitStatus;
import com.dietiestates.api.model.Visit;

public interface VisitRepository extends CrudRepository<Visit, Long> {
	
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
