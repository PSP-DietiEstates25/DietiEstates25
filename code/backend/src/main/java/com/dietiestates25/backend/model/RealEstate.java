package com.dietiestates25.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

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
@Table(name = "real_estates")
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private BigDecimal price;

    @NotNull
    private double size;

    @NotNull
    private int rooms;

    @NotNull
    private int floor;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EnergyClass energyClass;

    @NotNull
    @OneToOne(mappedBy = "realEstate")
    private Ad ad;

    @NotNull
    @OneToOne(mappedBy = "realEstate", cascade = CascadeType.ALL, orphanRemoval = false)
    private GeographicalPosition geographicalPosition;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "services_id")
    private Service services;
}