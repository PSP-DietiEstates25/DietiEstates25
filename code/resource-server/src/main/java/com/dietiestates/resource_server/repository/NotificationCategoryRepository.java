package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.enums.NotificationCategoryType;
import com.dietiestates.resource_server.model.NotificationCategory;
import com.dietiestates.resource_server.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationCategoryRepository extends CrudRepository<NotificationCategory, Long> {
    boolean existsById(Long id);
	Optional<NotificationCategory> findByName(NotificationCategoryType name);
    Optional<NotificationCategory> findByNameAndUserId(String name, Long userId);
    List<NotificationCategory> findByUserId(Long id);
}
