package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;
import com.dietiestates.api.model.RealEstateAd;

public interface RealEstateAdRepository extends CrudRepository<RealEstateAd, Long> {
    // Iterable<RealEstateAd> findByEstateAgent_Email(String email);
}