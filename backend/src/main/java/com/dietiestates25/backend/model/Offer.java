package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal offerPrice;

    @Column(nullable = false)
    private String status;
    // “INVIATA”, “ACCETTATA”, “RIFIUTATA”, “CONTROPROPOSTA”

    // RELAZIONI:

    // L’offerta è “effettuata da” (madeBy) un User (cliente) – ManyToOne
    @ManyToOne(optional = false)
    @JoinColumn(name = "made_by_user_id", nullable = false)
    private User madeBy;

    // L’offerta “riceve” risposta da un Agent (owner): il back-end potrà
    // inviare notifica a quell’agente oppure l’agente la vede nel suo cruscotto.
    // Ma in genere basta il “madeBy” e il “property” per capire chi deve gestire.

    @ManyToOne(optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    private RealEstate property;

    public Offer() {
    }

    public Offer(BigDecimal offerPrice, String status, User madeBy, RealEstate property) {
        this.offerPrice = offerPrice;
        this.status = status;
        this.madeBy = madeBy;
        this.property = property;
    }

    // GETTER / SETTER

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(BigDecimal offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getStatus() {
        return status;
    }   

    public void setStatus(String status) {
        this.status = status;
    }

    public User getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(User madeBy) {
        this.madeBy = madeBy;
    }

    public RealEstate getProperty() {
        return property;
    }

    public void setProperty(RealEstate property) {
        this.property = property;
    }
}