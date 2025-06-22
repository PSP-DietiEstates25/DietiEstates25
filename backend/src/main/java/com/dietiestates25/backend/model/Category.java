package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "notification_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private NotificationCategories name;

    private boolean isActive;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Notification> notifications = new HashSet<>();

    // Getters & setters
    public Long getId() {
        return id;
    }

    public NotificationCategories getName() {
        return name;
    }

    public void setName(NotificationCategories name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }
}
