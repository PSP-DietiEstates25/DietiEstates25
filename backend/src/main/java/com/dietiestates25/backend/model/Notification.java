package com.dietiestates25.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 1000)
    private String message;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @NotNull
    @Column(nullable = false)
    private String notificationType;
    // “NEW_PROPERTY”, “VISIT_RESPONSE”, “PROMOTIONAL”

    // RELAZIONE:
    // Un utente può ricevere molte notifiche – ManyToOne
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Notification() {
    }

    public Notification(String message, LocalDateTime dateCreated,
            String notificationType, boolean active, User user) {
        this.message = message;
        this.dateCreated = dateCreated;
        this.notificationType = notificationType;
        this.active = active;
        this.user = user;
    }

    // GETTER / SETTER

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}