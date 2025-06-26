package com.dietiestates25.backend.repository;

import com.dietiestates25.backend.model.Offer;
import com.dietiestates25.backend.model.RealEstate;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByRealEstateId(Long realEstateId);
    List<Offer> findByUserEmail(String userEmail);
    List<Offer> findByStatus(String status);
    List<Offer> findByEstate(RealEstate estate);
}
