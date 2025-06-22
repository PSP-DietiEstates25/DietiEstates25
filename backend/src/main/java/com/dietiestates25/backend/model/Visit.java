package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import io.micrometer.common.lang.NonNull;

@Entity
@Table(name = "visit")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estate_agent_id")
    private EstateAgent estateAgent;

    // Getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(@NonNull LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
    }

    public EstateAgent getEstateAgent() {
        return estateAgent;
    }

    public void setEstateAgent(EstateAgent estateAgent) {
        this.estateAgent = estateAgent;
    }
}