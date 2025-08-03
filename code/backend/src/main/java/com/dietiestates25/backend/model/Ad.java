package com.dietiestates25.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Rappresenta un annuncio (advertisement) pubblicato da un agente o da un
 * owner.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Dati dell'immobile
    @NotNull
    private BigDecimal price;

    @NotNull
    private double size;

    @NotNull
    private String address;

    @NotNull
    private int rooms;

    @NotNull
    private int floor;

    @Enumerated(EnumType.STRING)
    private EnergyClass energyClass;

    // Categoria annuncio
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdType category;

    // Extra (foto, descrizione)
    @NotNull
    private String photo;

    @NotNull
    private String description;

    private LocalDate deletedAt;

    // Relazioni
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estate_agent_id", nullable = false)
    private EstateAgent estateAgent;

    @NotNull
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proposal> proposals = new ArrayList<>();

    @NotNull
    @ManyToMany(mappedBy = "ads")
    private List<SavedSearch> savedSearches = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private EstateAgent publisher;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "position_id")
    private GeographicalPosition geographicalPosition;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services services;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();

    // Metodi:

    // Ritorna l'annuncio associato all'immobile
    public Ad getAd() {
        return this;
    }

    // Verifica se l'annuncio è ancora valido
    public boolean isActive() {
        return this.deletedAt == null || this.deletedAt.isAfter(LocalDate.now());
    }

    // Aggiunge un'offerta all'annuncio
    public void addOffer(Offer offer) {
        if (offer != null) {
            this.proposals.add(offer);
            // offer.setAd(this);
        }
    }
}