package com.dietiestates.api.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.enums.EnergyClass;
import com.dietiestates.api.model.RealEstateAd;

public interface RealEstateAdRepository extends CrudRepository<RealEstateAd, Long> {

    // dashboard agente (dal JWT)
    List<RealEstateAd> findByEstateAgent_Email(String email);

    // ricerca generale con parametri opzionali
    // q sta per query string
    @Query("""
            SELECT a FROM RealEstateAd a
            WHERE (:category IS NULL OR a.category = :category)
              AND (:q IS NULL OR
                   LOWER(a.description) LIKE LOWER(CONCAT('%', :q, '%')) OR
                   LOWER(a.address)     LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:minPrice IS NULL OR a.price >= :minPrice)
              AND (:maxPrice IS NULL OR a.price <= :maxPrice)
              AND (:minRooms IS NULL OR a.rooms >= :minRooms)
              AND (:energy IS NULL OR a.energyClass = :energy)
            """)
    List<RealEstateAd> search(
            @Param("category") AdCategory category,
            @Param("q") String q,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minRooms") Integer minRooms,
            @Param("energy") EnergyClass energy);
}