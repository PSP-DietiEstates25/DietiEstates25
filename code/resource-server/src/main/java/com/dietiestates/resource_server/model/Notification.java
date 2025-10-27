package com.dietiestates.resource_server.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 2000)
	private String message;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;

	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "notification_category_id",
			foreignKey = @ForeignKey(name = "NOTIFICATION_NOTIFICATION_CATEGORY_ID_FK"))
	private NotificationCategory notificationCategory;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "user_id", 
			foreignKey = @ForeignKey(name = "NOTIFICATION_USER_ID_FK"))
	private User user;

	@Builder(builderMethodName = "builder")
	public Notification(
			String message,
			NotificationCategory notificationCategory,
			User user
			) {
		this.message = message;
		notificationCategory.addNotification(this);
		user.addNotification(this);
	}
}
