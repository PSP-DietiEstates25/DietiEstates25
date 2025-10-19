package com.dietiestates.authserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.authserver.enums.RoleName;
import com.dietiestates.authserver.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Optional<Role> findByName(RoleName name);
}
