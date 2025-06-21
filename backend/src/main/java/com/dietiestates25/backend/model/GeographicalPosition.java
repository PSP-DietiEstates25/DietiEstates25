package com.dietiestates25.backend.model;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;

@Entity
public class GeographicalPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;

    @NonNull
    @Column
    private double latitude;

    @NonNull
    @Column
    private double longitude;
    
    @NonNull
    @Column
    private String city;
    
    @NonNull
    @Column
    private String municipality;

    @OneToOne(mappedBy = "geographicalPosition")
    private RealEstate realEstate;

    @OneToOne(mappedBy = "geographicalPosition")
    private Filters filters;
}
