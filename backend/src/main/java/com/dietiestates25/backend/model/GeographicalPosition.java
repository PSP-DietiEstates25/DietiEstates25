package com.dietiestates25.backend.model;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;

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

    // Getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NonNull double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NonNull double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(@NonNull String municipality) {
        this.municipality = municipality;
    }

    public double getAbitableRadius() {
        return abitableRadius;
    }

    public void setAbitableRadius(@NonNull double abitableRadius) {
        this.abitableRadius = abitableRadius;
    }

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    /* public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    } */
}
