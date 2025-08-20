package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;
import com.dietiestates.api.model.GeographicalPosition;

public interface GeographicalPositionRepository extends CrudRepository<GeographicalPosition, Long> {
}
