package com.dietiestates.resource_server.model;

import com.dietiestates.resource_server.enums.NotificationCategory;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationCategory notificationCategory;

    @Column(nullable = false)
    private Boolean isVisible;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "negotiation_id",
            foreignKey = @ForeignKey(name = "NOTIFICATION_NEGOTIATION_ID_FK"))
    private Negotiation negotiation;

	@Builder(builderMethodName = "builder")
	public Notification(
			String message,
            String notificationCategory,
            Boolean isVisible,
			Negotiation negotiation
			) {
		this.message = message;
        this.notificationCategory = NotificationCategory.valueOf(notificationCategory);
        this.isVisible = isVisible;
        negotiation.addNotification(this);
	}
}
