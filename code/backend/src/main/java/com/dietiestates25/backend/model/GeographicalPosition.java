package com.dietiestates25.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;

import jakarta.validation.constraints.NotNull;

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
@Table(name = "geographical_positions")
public class GeographicalPosition {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private double latitude;
    
    @NotNull
    private double longitude;
    
    @NotNull
    private String city;
    
    @NotNull
    private String municipality;
    
    @NotNull
    private double abitableRadius;

    @OneToOne(mappedBy = "geographicalPosition", fetch = FetchType.LAZY)
    private RealEstate realEstate;

    // @OneToOne(mappedBy = "geographicalPosition", fetch = FetchType.LAZY)
    // private Filters filters;

}
