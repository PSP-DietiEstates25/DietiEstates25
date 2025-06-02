package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.dietiestates25.model.RealEstate;

@Entity
@Table(name = "users") // “user” è parola riservata in alcuni DB, evitiamo
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;  // ADMIN, AGENT, OWNER, CLIENT

    // RELAZIONI:

    // Un utente può “inserire” molti immobili (se è agente/owner)
    @OneToMany(mappedBy = "insertedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RealEstate> properties = new ArrayList<>();

    // Un utente può “effettuare” (fare) molte offerte
    @OneToMany(mappedBy = "madeBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offersMade = new ArrayList<>();

    // Un utente (CLIENT) può “ricevere” offerte?
    // Se vogliamo che l’entity Offer tenga traccia di “toUser” (chi riceve l’offerta)
    // si può aggiungere una ManyToOne qui

    // Un utente può prenotare più visite (Visit)
    @OneToMany(mappedBy = "requestedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visitsRequested = new ArrayList<>();

    // L’utente può “visualizzare” ricerche salvate
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedSearch> savedSearches = new ArrayList<>();

    // L’utente riceve notifiche
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    // se hai altri ruoli (es. Owner crea agenti, Admin gestisce utenti), potresti aggiungere
    // relazioni aggiuntive (es. Owner→Agent), ma in questa versione di base le omettiamo.

    // Costruttori, getter e setter
    public User() { }

    public User(String email, String passwordHash, String username, UserRole role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.username = username;
        this.role = role;
    }

    // … getter / setter per tutti i campi …

    public enum UserRole {
        ADMIN, AGENT, OWNER, CLIENT
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
}