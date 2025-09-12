package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;
import com.dietiestates.api.model.Details;

public interface DetailsRepository extends CrudRepository<Details, Long> {
}