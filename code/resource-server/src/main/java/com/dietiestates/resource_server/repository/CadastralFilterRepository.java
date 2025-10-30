package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.CadastralFilter;
import org.springframework.data.repository.CrudRepository;

public interface CadastralFilterRepository extends CrudRepository<CadastralFilter, Long>{

    boolean existsById(Long id);

}
