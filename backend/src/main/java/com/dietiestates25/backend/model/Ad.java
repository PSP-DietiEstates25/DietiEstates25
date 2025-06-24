package com.dietiestates25.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "ad")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String photo;

    @NotNull
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdType category;

    @NotNull
    @OneToOne(mappedBy = "ad")
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
    @ManyToMany
    @JoinTable(
    		name = "ad_savedSearch",
    		joinColumns = @JoinColumn(name = "ad_id"),
    		inverseJoinColumns = @JoinColumn(name = "savedSearch_id")
    	)
    private List<SavedSearch> savedSearches = new ArrayList<>();

}