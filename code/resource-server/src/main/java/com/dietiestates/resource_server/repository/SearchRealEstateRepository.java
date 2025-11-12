package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.SearchRealEstate;
import com.dietiestates.resource_server.model.SearchRealEstateKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SearchRealEstateRepository extends CrudRepository<SearchRealEstate, SearchRealEstateKey>, PagingAndSortingRepository<SearchRealEstate, SearchRealEstateKey> {
    boolean existsById(Long id);

    //@EntityGraph(attributePaths = "realEstate")
    Page<SearchRealEstate> findBySearchId(Long searchId, Pageable pageable);
}
