package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "real_estates")
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Double dimensioni;   // in m²

    private String address;

    private Double latitude;
    private Double longitude;

    private Integer rooms;

    private String energyClass;   // Esempio: “A”, “B”, “C”, ecc.

    private LocalDateTime publicationDate;

    private String contractType;  // “vendita” o “affitto”

    private String status;        // “ATTIVO”, “VENDUTO”

    // RELAZIONI:

    // Quale agenzia pubblica questo immobile?
    @ManyToOne(optional = false)
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency; // non sappiamo se lasciare agency

    // Chi è l’utente (Agente/Owner) che ha inserito l’immobile?
    @ManyToOne(optional = false)
    @JoinColumn(name = "inserted_by", nullable = false)
    private User insertedBy;

    // Un immobile “offre” più servizi generici (es. portineria, climatizzazione…)
    @ManyToMany
    @JoinTable(
        name = "property_services",
        joinColumns = @JoinColumn(name = "property_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services = new ArrayList<>(); // Non sappiamo se fare un'entity Service abbia senso, forse si

    // Un immobile “ha” più servizi vicini (NearbyService)
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NearbyService> nearbyServices = new ArrayList<>(); // Non sappiamo se fare un'entity NearbyService abbia senso, forse si

    // Un immobile “riceve” molte offerte
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();

    // Un immobile può avere più visite prenotate
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();

    // Se vogliamo tenere traccia delle “SavedSearch” correlate,
    // possiamo aggiungere un @ManyToMany

    public RealEstate() { }

    // Costruttore base (elimina attributi facoltativi)
    public RealEstate(String title, String description, BigDecimal price, Double dimensioni, // ci sono 12 parametri, TROPPO! sonarqube ne consiglia massimo 7
                      String address, Double latitude, Double longitude,
                      Integer rooms, String energyClass,
                      LocalDateTime publicationDate, String contractType, String status) { 
        this.title = title;
        this.description = description;
        this.price = price;
        this.dimensioni = dimensioni;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rooms = rooms;
        this.energyClass = energyClass;
        this.publicationDate = publicationDate;
        this.contractType = contractType;
        this.status = status;
    }

    // … getter / setter …
}