package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import io.micrometer.common.lang.NonNull;

@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;

    @NonNull
    @Column
    private LocalDateTime requestDate;

    @NonNull
    @Column
    private String status;
    // “IN_ATTESA”, “CONFERMATA”, “RIFIUTATA”

    // RELAZIONI:

    // Un Visit è richiesto da un User (cliente) – ManyToOne
    @ManyToOne(optional = false)
    @JoinColumn(name = "requested_by_user_id")
    private User requestedBy;

    @ManyToOne
    @JoinColumn(name = "estate_agent_id")
    private EstateAgent estateAgent; // chi risponde

    // Un Visit è associato a un immobile (RealEstate) – ManyToOne
    @ManyToOne(optional = false)
    @JoinColumn(name = "property_id")
    private RealEstate property;

    public Visit() {
    }

    public Visit(LocalDateTime requestDate, String status, User requestedBy, RealEstate property) {
        this.requestDate = requestDate;
        this.status = status;
        this.requestedBy = requestedBy;
        this.property = property;
    }

    // GETTER / SETTER

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(User requestedBy) {
        this.requestedBy = requestedBy;
    }

    public RealEstate getProperty() {
        return property;
    }

    public void setProperty(RealEstate property) {
        this.property = property;
    }
}
