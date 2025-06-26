package com.dietiestates25.backend.repository;

import com.dietiestates25.backend.model.Notification;
import com.dietiestates25.backend.model.NotificationCategory;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserEmail(String userEmail);
    List<Notification> findByCategory(NotificationCategory category);
}
