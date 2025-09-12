package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;
import com.dietiestates.api.model.Utility;

public interface ServicesRepository extends CrudRepository<Utility, Long> {
}
