package com.dietiestates25.backend.repository;

import com.dietiestates25.backend.model.Visit;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByRealEstateId(Long realEstateId);
    List<Visit> findByUserEmail(String userEmail);
    List<Visit> findByVisitDateAfter(Instant date);
}

