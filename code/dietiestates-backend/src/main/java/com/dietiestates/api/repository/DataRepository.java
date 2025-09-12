package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.api.model.Data;

public interface DataRepository extends CrudRepository<Data, Long> {

}
