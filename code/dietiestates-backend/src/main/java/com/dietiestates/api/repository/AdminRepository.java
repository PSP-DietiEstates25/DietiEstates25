package com.dietiestates.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dietiestates.api.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUserId(Long userId);
}