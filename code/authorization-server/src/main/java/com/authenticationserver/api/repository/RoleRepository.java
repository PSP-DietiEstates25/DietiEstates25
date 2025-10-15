package com.authenticationserver.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.authenticationserver.api.enums.RoleName;
import com.authenticationserver.api.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Optional<Role> findByName(RoleName name);
}
