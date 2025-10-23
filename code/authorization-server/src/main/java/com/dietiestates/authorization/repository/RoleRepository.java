package com.dietiestates.authorization.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.authorization.enums.RoleName;
import com.dietiestates.authorization.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Optional<Role> findByName(RoleName name);
}
