package com.dietiestates25.backend.repository;

import com.dietiestates25.backend.model.Ad;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByType(String type);
    List<Ad> findByPriceBetween(BigDecimal min, BigDecimal max);
    List<Ad> findByRooms(int rooms);
    List<Ad> findByEnergyClass(String energyClass);
    // metterli in ad o in real estate?
    List<Ad> findByRealEstateId(Long realEstateId);

    @Query("SELECT a FROM Ad a WHERE a.deletedAt IS NULL")
    List<Ad> findActiveAds();
}
