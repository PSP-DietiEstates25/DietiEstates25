package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un annuncio (advertisement) pubblicato da un agente o da un
 * owner.
 * Contiene i dettagli “pubblicitari” (titolo, descrizione, prezzo, data di
 * pubblicazione,
 * tipo di contratto, stato) e “punta” all’immobile strutturale (RealEstate).
 */
@Entity
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, length = 20)
    private String contractType; // “vendita” o “affitto”

    @Column(nullable = false, length = 20)
    private String status; // “ATTIVO”, “VENDUTO”

    // RELAZIONI

    // Un annuncio è inserito da un utente (Agente o Owner).
    @ManyToOne(optional = false)
    @JoinColumn(name = "inserted_by_user", nullable = false)
    private User insertedBy;

    // @ManyToOne(optional = false)
    // @JoinColumn(name = "agency_id", nullable = false)
    // private Agency agency; // ha senso se vogliamo tenere traccia dell'agenzia che pubblica l'immobile, altrimenti possiamo ometterlo

    // Un annuncio punta a un immobile strutturale (RealEstate).
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "real_estate_id", nullable = false, unique = true)
    private RealEstate realEstate;

    // Un annuncio può ricevere più offerte (Offer) da parte di clienti.
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offersReceived = new ArrayList<>();

    // Un annuncio può avere più visite prenotate (Visit).
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();

    public Ad() {
    }

    /**
     * Costruttore “sintetico”.
     * Se l’immobile strutturale venisse inserito a parte, potremmo passare
     * l’istanza di RealEstate
     * a questo costruttore solo quando creiamo l’annuncio.
     */
    public Ad(String title,
            String description,
            BigDecimal price,
            String contractType,
            String status,
            User insertedBy,
            // Agency agency,
            RealEstate realEstate) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.contractType = contractType;
        this.status = status;
        this.insertedBy = insertedBy;
        // this.agency = agency;
        this.realEstate = realEstate;
    }

    // GETTER / SETTER

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(User insertedBy) {
        this.insertedBy = insertedBy;
    }

    // public Agency getAgency() {
    //     return agency;
    // }

    // public void setAgency(Agency agency) {
    //     this.agency = agency;
    // }

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    public List<Offer> getOffersReceived() {
        return offersReceived;
    }

    public void setOffersReceived(List<Offer> offersReceived) {
        this.offersReceived = offersReceived;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }
}