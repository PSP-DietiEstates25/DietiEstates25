package com.dietiestates.api.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.dietiestates.api.model.Search;

public interface SearchRepository extends
	CrudRepository<Search, Long>,
	PagingAndSortingRepository<Search, Long>{

	List<Search> findByUser(String userEmail, Pageable pageable);
}
