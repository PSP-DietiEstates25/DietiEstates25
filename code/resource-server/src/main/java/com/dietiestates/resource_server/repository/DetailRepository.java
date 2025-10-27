package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.Detail;
import org.springframework.data.repository.CrudRepository;

public interface DetailRepository extends CrudRepository<Detail, Long> {
}