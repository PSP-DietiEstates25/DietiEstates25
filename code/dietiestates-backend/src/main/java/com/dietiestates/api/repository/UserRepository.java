package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.api.model.User;

public interface UserRepository extends CrudRepository<User, String> {

}
