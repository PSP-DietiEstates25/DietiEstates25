package com.dietiestates.resourceserver.repository;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.resourceserver.model.SearchRealEstate;
import com.dietiestates.resourceserver.model.SearchRealEstateKey;

public interface SearchRealEstateRepository extends CrudRepository<SearchRealEstate, SearchRealEstateKey>{

}
