package com.dietiestates25.backend.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@DiscriminatorValue("ESTATE_AGENT")
@Table(name = "estate_agent")
public class EstateAgent extends Staffer {
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin nominatedBy;

    @OneToMany(mappedBy = "estateAgent", cascade = CascadeType.ALL)
    private Set<Offer> advancedOffers = new HashSet<>();

    @OneToMany(mappedBy = "estateAgent", cascade = CascadeType.ALL)
    private Set<Visit> respondedVisits = new HashSet<>();

}
