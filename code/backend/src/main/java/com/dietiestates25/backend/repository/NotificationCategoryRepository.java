package com.dietiestates25.backend.repository;

import com.dietiestates25.backend.model.NotificationCategory;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface NotificationCategoryRepository extends JpaRepository<NotificationCategory, com.dietiestates25.backend.model.NotificationCategoryType> {

}

