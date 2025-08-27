package com.dietiestates.api.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.enums.NotificationCategoryType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notification_preference", uniqueConstraints = @UniqueConstraint(name = "UK_notif_pref_user_cat", columnNames = {
        "user_id", "category" }))
@EntityListeners(AuditingEntityListener.class)
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationCategoryType category;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
    		nullable = false,
    		name = "user_id", 
    		foreignKey = @ForeignKey(name = "NOTIF_PREF_USER_ID_FK"))
    private User user;
}
