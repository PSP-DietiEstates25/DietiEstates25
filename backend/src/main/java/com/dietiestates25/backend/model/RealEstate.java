package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;
import java.math.BigDecimal;

/*
 * Rappresenta l’immobile “fisico” i cui attributi non cambiano (o cambiano raramente):
 * dimensioni, indirizzo, posizione geografica, stanze, classe energetica, servizi
 * generici (portineria, climatizzazione, etc.) e servizi vicini (NearbyService)
 * ha relazione 1:1 obbligatoria con un Ad. 
 */

@Entity
@Table(name = "real_estate")
public class RealEstate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal price;
    private double size;
    private String address;
    private int rooms;
    private int floor;

    @Enumerated(EnumType.STRING)
    private EnergyClass energyClass;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "services_id")
    private Service services;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "geo_position_id")
    private GeographicalPosition geographicalPosition;

    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ad> ads = new HashSet<>();

    // Getters & setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public EnergyClass getEnergyClass() {
        return energyClass;
    }

    public void setEnergyClass(EnergyClass energyClass) {
        this.energyClass = energyClass;
    }

    public Service getServices() {
        return services;
    }

    public void setServices(Service services) {
        this.services = services;
    }

    public GeographicalPosition getGeographicalPosition() {
        return geographicalPosition;
    }

    public void setGeographicalPosition(GeographicalPosition geographicalPosition) {
        this.geographicalPosition = geographicalPosition;
    }

    public Set<Ad> getAds() {
        return ads;
    }

    public void setAds(Set<Ad> ads) {
        this.ads = ads;
    }
}