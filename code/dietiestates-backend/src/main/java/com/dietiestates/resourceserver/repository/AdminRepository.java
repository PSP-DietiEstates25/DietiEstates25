package com.dietiestates.resourceserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.resourceserver.model.Admin;

public interface AdminRepository extends CrudRepository<Admin, Long> {

	Optional<Admin> findByEmail(String email);
}
