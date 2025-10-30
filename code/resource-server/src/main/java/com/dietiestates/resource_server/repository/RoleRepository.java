package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	Optional<Role> existsByName(String role);
    Optional<Role> findByName(String name);
}
