package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.CadastralData;
import org.springframework.data.repository.CrudRepository;

public interface CadastralDataRepository extends CrudRepository<CadastralData, Long> {
    boolean existsById(Long id);

}
