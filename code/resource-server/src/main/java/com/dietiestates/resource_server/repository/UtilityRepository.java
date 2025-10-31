package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.Utility;
import org.springframework.data.repository.CrudRepository;

public interface UtilityRepository extends CrudRepository<Utility, Long> {

    boolean existsById(Long id);

}
