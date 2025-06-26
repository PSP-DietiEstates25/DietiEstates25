package com.dietiestates25.backend.repository;

import com.dietiestates25.backend.model.GeographicalPosition;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeographicalPositionRepository extends JpaRepository<GeographicalPosition, Long> {
    List<GeographicalPosition> findByCity(String city);
    List<GeographicalPosition> findByLatitudeBetweenAndLongitudeBetween(
        double latMin, double latMax, double lonMin, double lonMax);
}
