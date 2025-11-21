package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.FileData;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FileDataRepository extends CrudRepository<FileData, String> {
    Optional<FileData> findByName(String name);
}
