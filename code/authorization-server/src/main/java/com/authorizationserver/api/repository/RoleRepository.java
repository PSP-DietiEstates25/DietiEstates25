package com.authorizationserver.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.authorizationserver.api.enums.RoleName;
import com.authorizationserver.api.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Optional<Role> findByName(RoleName name);
}
