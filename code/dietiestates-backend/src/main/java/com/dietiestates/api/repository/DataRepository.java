package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.api.model.CadastralData;

public interface DataRepository extends CrudRepository<CadastralData, Long> {

}
