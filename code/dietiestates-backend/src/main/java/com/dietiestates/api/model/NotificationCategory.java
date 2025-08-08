package com.dietiestates.api.model;

import java.util.ArrayList;
import java.util.List;

import com.dietiestates.api.enums.NotificationCategoryType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class NotificationCategory {

	@Id
	@Enumerated(EnumType.STRING)
	private NotificationCategoryType name;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private Boolean isActive = true;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@OneToMany(mappedBy = "notificationCategory", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notification> notifications = new ArrayList<>();
	
	public NotificationCategory(NotificationCategoryType name) {
		this.name = name;
	}
	
	public NotificationCategory(NotificationCategoryType name, Boolean isActive) {
		this.name = name;
		this.isActive = isActive;
	}
	
	public void addNotification(Notification notification) {
		notifications.add(notification);
		notification.setNotificationCategory(this);
	}
	
	public Boolean removeNotification(Notification notification) {
		return notifications.remove(notification);
	}
}
