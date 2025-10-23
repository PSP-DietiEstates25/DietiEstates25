package com.dietiestates.resourceserver.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.dietiestates.resourceserver.model.RealEstate;

public interface RealEstateRepository extends 
	CrudRepository<RealEstate, Long>,
	PagingAndSortingRepository<RealEstate, Long>{

}
