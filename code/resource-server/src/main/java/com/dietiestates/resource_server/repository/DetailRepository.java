package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.Detail;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface DetailRepository extends CrudRepository<Detail, Long> {
    boolean existsById(Long id);
    boolean existsByGeographicalPositionId(Long geographicalPositionId);
    boolean existsByUtilityId(Long utilityId);
    Optional<Detail> findByGeographicalPositionId(Long geographicalPositionId);
    Optional<Detail> findByUtilityId(Long utilityId);
}