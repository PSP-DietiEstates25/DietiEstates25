package com.dietiestates25.backend.model;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;

@Entity
@Table(name = "notification")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String message;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@ManyToMany(mappedBy = "seenNotifications")
	private Set<User> seenBy = new HashSet<>();

	// Getters & setters
	public Long getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<User> getSeenBy() {
		return seenBy;
	}

	public void setSeenBy(Set<User> seenBy) {
		this.seenBy = seenBy;
	}
}
