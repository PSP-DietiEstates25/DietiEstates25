package com.dietiestates.resourceserver.repository;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.resourceserver.model.GeographicalPosition;

public interface GeographicalPositionRepository extends CrudRepository<GeographicalPosition, Long> {
}
