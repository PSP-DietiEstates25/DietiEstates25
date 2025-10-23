package com.dietiestates.resourceserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.resourceserver.enums.NotificationCategoryType;
import com.dietiestates.resourceserver.model.NotificationCategory;

public interface NotificationCategoryRepository extends CrudRepository<NotificationCategory, Long> {
	
	Optional<NotificationCategory> findByName(NotificationCategoryType name);

}
