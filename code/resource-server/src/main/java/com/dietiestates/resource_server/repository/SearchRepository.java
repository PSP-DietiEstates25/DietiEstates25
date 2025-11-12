package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SearchRepository extends CrudRepository<Search, Long>, PagingAndSortingRepository<Search, Long>{
    boolean existsById(Long id);
	Page<Search> findByUserId(Long id, Pageable pageable);
}
