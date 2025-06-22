package com.dietiestates25.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import java.util.HashSet;
import java.util.Set;

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
    
    private String photo;
    
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdCategories category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "real_estate_id", nullable = false)
    private RealEstate realEstate;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Offer> offers = new HashSet<>();
    
}