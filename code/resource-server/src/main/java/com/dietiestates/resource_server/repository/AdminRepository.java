package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin, Long> {

	Optional<Admin> findByEmail(String email);
}
