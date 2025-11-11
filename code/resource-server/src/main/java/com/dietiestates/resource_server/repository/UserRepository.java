package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsById(Long id);
	Optional<User> findByEmail(String email);
}
