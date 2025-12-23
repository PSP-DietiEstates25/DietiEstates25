package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.GeographicalPosition;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface GeographicalPositionRepository extends CrudRepository<GeographicalPosition, Long> {
    boolean existsById(Long id);
    Optional<GeographicalPosition> findByRegionAndCityAndMunicipality(
            String region,
            String city,
            String municipality
    );
}
