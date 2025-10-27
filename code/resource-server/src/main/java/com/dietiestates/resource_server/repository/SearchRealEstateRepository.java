package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.SearchRealEstate;
import com.dietiestates.resource_server.model.SearchRealEstateKey;
import org.springframework.data.repository.CrudRepository;

public interface SearchRealEstateRepository extends CrudRepository<SearchRealEstate, SearchRealEstateKey>{

}
