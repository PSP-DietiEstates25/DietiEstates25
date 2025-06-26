package com.dietiestates25.backend.repository;

import com.dietiestates25.backend.model.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    @Query("SELECT a FROM Admin a WHERE a.email = :email")
    Admin findAdminByEmail(@Param("email") String email);
}