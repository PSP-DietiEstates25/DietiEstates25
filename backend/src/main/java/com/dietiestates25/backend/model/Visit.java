package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime requestDate; 

    @Column(nullable = false)
    private String status;  
    // “IN_ATTESA”, “CONFERMATA”, “RIFIUTATA”

    // RELAZIONI:

    // Un Visit è richiesto da un User (cliente) – ManyToOne
    @ManyToOne(optional = false)
    @JoinColumn(name = "requested_by_user_id", nullable = false)
    private User requestedBy;

    // Un Visit è “ricevuto” da un agente o all’amministrazione?  
    // Se vogliamo tenere traccia di “a chi è spettato gestire la visita”, potremmo
    // aggiungere un @ManyToOne verso User (ruolo AGENT)
    // il “requestedBy” è sufficiente per mostrare allo stesso utente lo stato.

    // Un Visit è associato a un immobile (RealEstate) – ManyToOne
    @ManyToOne(optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    private RealEstate property;

    public Visit() { }

    public Visit(LocalDateTime requestDate, String status, User requestedBy, RealEstate property) {
        this.requestDate = requestDate;
        this.status = status;
        this.requestedBy = requestedBy;
        this.property = property;
    }

    // getter / setter …
}
