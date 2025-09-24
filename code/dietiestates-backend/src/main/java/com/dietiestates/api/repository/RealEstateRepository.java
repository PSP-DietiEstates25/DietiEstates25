package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.dietiestates.api.model.RealEstate;

public interface RealEstateRepository extends 
	CrudRepository<RealEstate, Long>,
	PagingAndSortingRepository<RealEstate, Long>{

}
