package com.dietiestates.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.dietiestates.api.model.NotificationCategory;
//import com.dietiestates.api.model.NotificationPreference;

public interface NotificationPreferenceRepository extends CrudRepository</*NotificationPreference*/NotificationCategory, Long> {

    //Optional<NotificationPreference> findByUser_EmailAndCategory(String email, NotificationCategoryType category);

    //List<NotificationPreference> findAllByUser_Email(String email);
}
