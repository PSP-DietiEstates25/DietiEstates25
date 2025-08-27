package com.dietiestates.api.model;

import java.time.Instant;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.enums.NotificationCategoryType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.EnumType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "notification")
@EntityListeners(AuditingEntityListener.class)
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.STRING)
	private NotificationCategoryType category;

	@NotBlank
	private String title;

	@NotBlank
	@Column(length = 2000)
	private String message;

	
	private Long adId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			nullable = false,
			name = "user_id", 
			foreignKey = @ForeignKey(name = "NOTIF_USER_ID_FK"))
	private User user;

	private Boolean readFlag;

	private Instant createdAt;

	@PrePersist
	void prePersist() {
		if (createdAt == null)
			createdAt = Instant.now();
		if (readFlag == null)
			readFlag = Boolean.FALSE;
	}
}
