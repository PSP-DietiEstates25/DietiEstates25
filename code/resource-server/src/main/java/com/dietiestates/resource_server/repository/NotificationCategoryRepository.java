package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.enums.NotificationCategoryType;
import com.dietiestates.resource_server.model.NotificationCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NotificationCategoryRepository extends CrudRepository<NotificationCategory, Long> {
	
	Optional<NotificationCategory> findByName(NotificationCategoryType name);

}
