package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;
import com.dietiestates.api.model.Detail;

public interface DetailRepository extends CrudRepository<Detail, Long> {
}