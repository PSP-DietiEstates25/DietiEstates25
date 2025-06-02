package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @Column(nullable = false)
    private String notificationType;
    // “NEW_PROPERTY”, “VISIT_RESPONSE”, “PROMOTIONAL”

    @Column(nullable = false)
    private boolean active;  // true => attiva, false => disattivata

    // RELAZIONE:
    // Un utente può ricevere molte notifiche – ManyToOne
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Notification() { }

    public Notification(String message, LocalDateTime dateCreated, 
                        String notificationType, boolean active, User user) {
        this.message = message;
        this.dateCreated = dateCreated;
        this.notificationType = notificationType;
        this.active = active;
        this.user = user;
    }

    // getter / setter …
}