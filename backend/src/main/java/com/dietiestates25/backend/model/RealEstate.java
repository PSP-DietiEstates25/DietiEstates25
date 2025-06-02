package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Rappresenta l’immobile “fisico” i cui attributi non cambiano (o cambiano raramente):
 * dimensioni, indirizzo, posizione geografica, stanze, classe energetica, servizi
 * generici (portineria, climatizzazione, etc.) e servizi vicini (NearbyService)
 * ha relazione 1:1 obbligatoria con un Ad. 
 */

@Entity
@Table(name = "real_estates")
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false)
    private Double dimensioni; // m²

    private Double latitude;
    private Double longitude;

    private Integer rooms;

    private String energyClass; // Esempio: “A”, “B”, “C”, ecc.

    @Column(length = 100)
    private String category; // “apartment”, “house” (per futura evoluzione)

    // RELAZIONI:

    // Chi è l’utente (Agente/Owner) che ha inserito l’immobile?
    @ManyToOne(optional = false)
    @JoinColumn(name = "inserted_by", nullable = false)
    private User insertedBy;

    // Un immobile “offre” più servizi generici (es. portineria, climatizzazione…)
    @ManyToMany
    @JoinTable(name = "property_services", joinColumns = @JoinColumn(name = "property_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<Service> services = new ArrayList<>();

    // Un immobile “ha” più servizi vicini (NearbyService)
    // @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // private List<NearbyService> nearbyServices = new ArrayList<>(); // ha senso
    // se vogliamo tenere traccia dei servizi vicini?

    // Un immobile “riceve” molte offerte
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();

    // Un immobile può avere più visite prenotate
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();

    // Un immobile può essere “associato” a un annuncio (Ad)
    @OneToOne(mappedBy = "realEstate", fetch = FetchType.LAZY)
    private Ad ad;

    // Se vogliamo tenere traccia delle “SavedSearch” correlate,
    // possiamo aggiungere un @ManyToMany

    public RealEstate() {
    }

    public RealEstate(String address,
            Double dimensioni,
            Double latitude,
            Double longitude,
            Integer rooms,
            String energyClass,
            String category) {

        this.address = address;
        this.dimensioni = dimensioni;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rooms = rooms;
        this.energyClass = energyClass;
        this.category = category;
    }

    // GETTER / SETTER
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getDimensioni() {
        return dimensioni;
    }

    public void setDimensioni(Double dimensioni) {
        this.dimensioni = dimensioni;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public String getEnergyClass() {
        return energyClass;
    }

    public void setEnergyClass(String energyClass) {
        this.energyClass = energyClass;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public User getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(User insertedBy) {
        this.insertedBy = insertedBy;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RealEstate))
            return false;
        RealEstate that = (RealEstate) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31; 
    }
}