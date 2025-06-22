package com.dietiestates25.backend.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("ESTATE_AGENT")
@Table(name = "estate_agent")
public class EstateAgent /*extends Staffer*/ {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin nominatedBy;

    @OneToMany(mappedBy = "estateAgent", cascade = CascadeType.ALL)
    private Set<Offer> advancedOffers = new HashSet<>();

    @OneToMany(mappedBy = "estateAgent", cascade = CascadeType.ALL)
    private Set<Visit> respondedVisits = new HashSet<>();

    public Admin getNominatedBy() {
        return nominatedBy;
    }

    public void setNominatedBy(Admin nominatedBy) {
        this.nominatedBy = nominatedBy;
    }

    public Set<Offer> getAdvancedOffers() {
        return advancedOffers;
    }

    public void setAdvancedOffers(Set<Offer> advancedOffers) {
        this.advancedOffers = advancedOffers;
    }

    public Set<Visit> getRespondedVisits() {
        return respondedVisits;
    }

    public void setRespondedVisits(Set<Visit> respondedVisits) {
        this.respondedVisits = respondedVisits;
    }
}
