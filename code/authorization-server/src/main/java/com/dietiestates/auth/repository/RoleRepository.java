package com.dietiestates.auth.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.auth.enums.RoleName;
import com.dietiestates.auth.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
