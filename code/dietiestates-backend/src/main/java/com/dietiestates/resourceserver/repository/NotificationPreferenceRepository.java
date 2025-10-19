package com.dietiestates.resourceserver.repository;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.resourceserver.model.NotificationCategory;

public interface NotificationPreferenceRepository extends CrudRepository</*NotificationPreference*/NotificationCategory, Long> {

    //Optional<NotificationPreference> findByUser_EmailAndCategory(String email, NotificationCategoryType category);

    //List<NotificationPreference> findAllByUser_Email(String email);
}
