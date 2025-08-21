package com.dietiestates.api.repository;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dietiestates.api.enums.AdCategory;
import com.dietiestates.api.enums.EnergyClass;
import com.dietiestates.api.model.RealEstateAd;

public interface RealEstateAdRepository extends CrudRepository<RealEstateAd, Long> {

  @Query("""
      SELECT a FROM RealEstateAd a
      WHERE (:category IS NULL OR a.category = :category)
        AND (
              :q IS NULL OR
              LOWER(a.description) LIKE LOWER(CONCAT('%', :q, '%')) OR
              LOWER(a.address) LIKE LOWER(CONCAT('%', :q, '%'))
            )
        AND (:minPrice IS NULL OR a.price >= :minPrice)
        AND (:maxPrice IS NULL OR a.price <= :maxPrice)
        AND (:minRooms IS NULL OR a.rooms >= :minRooms)
        AND (:energy IS NULL OR a.energyClass = :energy)
      """)
  Page<RealEstateAd> search(
      @Param("category") AdCategory category,
      @Param("q") String q,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("minRooms") Integer minRooms,
      @Param("energy") EnergyClass energy,
      Pageable pageable);

  @EntityGraph(attributePaths = { "detail" })
  Page<RealEstateAd> findByPostedBy_Email(String email, Pageable pageable);
}
