package com.dietiestates25.backend.repository;

import com.dietiestates25.backend.model.Ad;
import com.dietiestates25.backend.model.AdType;
import com.dietiestates25.backend.model.RealEstate;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {
    List<Ad> findByType(String type);
    List<Ad> findByPriceBetween(BigDecimal min, BigDecimal max);
    List<Ad> findByRooms(int rooms);
    List<Ad> findByEnergyClass(String energyClass);
    List<RealEstate> findByType(AdType type);
}
