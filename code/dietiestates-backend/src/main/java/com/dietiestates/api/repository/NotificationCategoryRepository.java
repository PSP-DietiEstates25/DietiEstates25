package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.api.enums.NotificationCategoryType;
import com.dietiestates.api.model.NotificationCategory;

public interface NotificationCategoryRepository extends CrudRepository<NotificationCategory, NotificationCategoryType> {

}