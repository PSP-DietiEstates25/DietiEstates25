package com.dietiestates.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.api.model.Admin;

public interface AdminRepository extends CrudRepository<Admin, Long> {

	Optional<Admin> findByEmail(String email);
}
