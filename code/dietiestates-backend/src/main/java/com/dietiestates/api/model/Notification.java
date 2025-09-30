package com.dietiestates.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
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
			foreignKey = @ForeignKey(name = "NOTIFICATION_NOTIFICATION_CATEGORY_ID"))
	private NotificationCategory notificationCategory;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "user_id", 
			foreignKey = @ForeignKey(name = "NOTIFICATION_USER_ID_FK"))
	private User user;

}
