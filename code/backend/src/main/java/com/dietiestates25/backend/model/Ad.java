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
 * Contiene i dettagli “pubblicitari” (titolo, descrizione, prezzo, data di
 * pubblicazione,
 * tipo di contratto, stato) e “punta” all’immobile strutturale (RealEstate).
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
    
    @NotNull
    private String photo;

    @NotNull
    private String description;

    private LocalDate deletedAt;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdType category;

    @NotNull
    @OneToOne
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estate_agent_id", nullable = false)
    private EstateAgent estateAgent;

    @NotNull
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();

    @NotNull
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();
 
    @NotNull
    @ManyToMany(mappedBy = "ads")
    private List<SavedSearch> savedSearches = new ArrayList<>();


    // Metodi:

    // Verifica se l'annuncio è ancora valido
    public boolean isActive() {
        return this.deletedAt == null || this.deletedAt.isAfter(LocalDate.now());
    }

    // Aggiunge un'offerta all'annuncio
    public void addOffer(Offer offer) {
        if (offer != null) {
            this.offers.add(offer);
            offer.setAd(this);
        }
    }

    public BigDecimal getPrice() {
        return this.realEstate != null ? this.realEstate.getPrice() : BigDecimal.ZERO;
    }
}