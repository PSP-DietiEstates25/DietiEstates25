package com.dietiestates25.backend.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "notification_categories")
public class NotificationCategory {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    @NaturalId
    @Enumerated(EnumType.STRING)
    private NotificationCategoryType name;

    @NotNull
    private boolean isActive;

    @OneToMany(mappedBy = "notificationCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    // Metodi:

    public void markAsRead() {
        this.isActive = true;
    }

    public void markAsUnread() {
        this.isActive = false;
    }

}
