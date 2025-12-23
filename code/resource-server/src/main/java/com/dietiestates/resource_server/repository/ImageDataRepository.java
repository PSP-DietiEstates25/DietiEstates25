package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.ImageData;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface ImageDataRepository extends CrudRepository<ImageData, Long> {
    Optional<ImageData> findByName(String name);
}
