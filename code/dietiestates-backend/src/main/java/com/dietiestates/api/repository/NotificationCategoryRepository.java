package com.dietiestates.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.api.enums.NotificationCategoryType;
import com.dietiestates.api.model.NotificationCategory;

public interface NotificationCategoryRepository extends CrudRepository<NotificationCategory, Long> {
	
	Optional<NotificationCategory> findByName(NotificationCategoryType name);

}
