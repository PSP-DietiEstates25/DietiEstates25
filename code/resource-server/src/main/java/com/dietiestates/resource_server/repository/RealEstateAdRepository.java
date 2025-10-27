package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.RealEstate;
import org.springframework.data.repository.CrudRepository;

public interface RealEstateAdRepository extends CrudRepository<RealEstate, Long> {

	/*
  @Query("""
      SELECT a FROM RealEstate a
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
  Page<RealEstate> search(
      @Param("category") AdCategory category,
      @Param("q") String q,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("minRooms") Integer minRooms,
      @Param("energy") EnergyClass energy,
      Pageable pageable);

  @EntityGraph(attributePaths = { "detail" })
  Page<RealEstate> findByEstateAgent_Email(String email, Pageable pageable);
  */
}
