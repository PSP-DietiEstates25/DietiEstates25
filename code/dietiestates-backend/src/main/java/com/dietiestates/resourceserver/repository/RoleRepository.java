package com.dietiestates.resourceserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.resourceserver.model.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	Optional<Role> findByName(String role);
}
