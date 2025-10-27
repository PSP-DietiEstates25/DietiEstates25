package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.RealEstate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RealEstateRepository extends 
	CrudRepository<RealEstate, Long>,
	PagingAndSortingRepository<RealEstate, Long>{

}
