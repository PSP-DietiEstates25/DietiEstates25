package com.dietiestates.resource_server.repository;

import com.dietiestates.resource_server.model.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface NotificationRepository extends 
	CrudRepository<Notification, Long>,
	PagingAndSortingRepository<Notification, Long> {

	List<Notification> findAllByUser(String userEmail, Pageable pageable);
	/*
    Page<Notification> findByUser_EmailOrderByCreatedAtDesc(String email, Pageable pageable);

    Page<Notification> findByUser_EmailAndReadFlagFalseOrderByCreatedAtDesc(String email, Pageable pageable);

    long countByUser_EmailAndReadFlagFalse(String email);

    boolean existsByIdAndUser_Email(Long id, String email);

    void deleteByIdAndUser_Email(Long id, String email);
    */
}
