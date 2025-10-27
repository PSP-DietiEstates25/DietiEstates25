package com.dietiestates.resource_server.model;

import com.dietiestates.resource_server.enums.NotificationCategoryType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@EntityListeners(AuditingEntityListener.class)
public class NotificationCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	@Enumerated(EnumType.STRING)
	private NotificationCategoryType name;
	
	@Column(nullable = false)
	private Boolean isActive;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToMany(mappedBy = "notificationCategory", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Notification> notifications = new ArrayList<>();
	
	@Builder(builderMethodName = "builder")
	public NotificationCategory(
			String name,
			Boolean isActive
			) {
		this.name = NotificationCategoryType.valueOf(name);
		this.isActive = isActive;
	}
	
	public void addNotification(Notification notification) {
		notifications.add(notification);
		notification.setNotificationCategory(this);
	}
}
