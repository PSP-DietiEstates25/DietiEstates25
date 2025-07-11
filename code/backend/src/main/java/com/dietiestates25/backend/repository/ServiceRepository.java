package com.dietiestates25.backend.repository;

import com.dietiestates25.backend.model.Services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {
    Services findByName(String name);
}
