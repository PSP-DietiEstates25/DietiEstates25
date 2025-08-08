package com.dietiestates.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Notification {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private String message;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "notification_category_name",
			foreignKey = @ForeignKey(name = "NOTIFICATION_CATEGORY_NAME_FK"))
	private NotificationCategory notificationCategory;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "user_email",
			foreignKey = @ForeignKey(name = "USER_EMAIL_FK"))
	private User user;
	
	public Notification(String message, NotificationCategory notificationCategory) {
		this.message = message;
		this.notificationCategory = notificationCategory;
	}
}
