package com.dietiestates.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.api.model.SearchRealEstate;
import com.dietiestates.api.model.SearchRealEstateKey;

public interface SearchRealEstateRepository extends CrudRepository<SearchRealEstate, SearchRealEstateKey>{

}
