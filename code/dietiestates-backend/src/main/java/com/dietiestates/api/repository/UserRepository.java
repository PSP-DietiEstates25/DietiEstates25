package com.dietiestates.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.dietiestates.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySecurityAccountDecorator_Id(Long accountId);

}
