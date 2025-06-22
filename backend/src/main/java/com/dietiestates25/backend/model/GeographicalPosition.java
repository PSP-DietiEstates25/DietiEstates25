package com.dietiestates25.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;

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
public class GeographicalPosition {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double latitude;
    
    private double longitude;
    
    private String city;
    
    private String municipality;
    
    private double abitableRadius;

    @OneToOne(mappedBy = "geographicalPosition", fetch = FetchType.LAZY)
    private RealEstate realEstate;

    // @OneToOne(mappedBy = "geographicalPosition", fetch = FetchType.LAZY)
    // private Filters filters;

}
