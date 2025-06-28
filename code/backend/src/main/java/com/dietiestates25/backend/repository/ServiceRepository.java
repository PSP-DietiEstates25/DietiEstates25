package com.dietiestates25.backend.repository;

import com.dietiestates25.backend.model.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Service findByName(String name);
}
