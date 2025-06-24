package com.dietiestates25.backend.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.math.BigDecimal;

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
@Table(name = "real_estate")
public class RealEstate {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ad_id")
    private Ad ad;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "services_id")
    private Service services;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "geographical_position_id")
    private GeographicalPosition geographicalPosition;

    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ad> ads = new HashSet<>();

}