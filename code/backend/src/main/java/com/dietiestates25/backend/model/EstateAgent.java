package com.dietiestates25.backend.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@DiscriminatorValue("ESTATE_AGENT")
@Table(name = "estate_agent")
public class EstateAgent extends Staffer {
	
	@NotNull
	private StafferRole role = StafferRole.ESTATE_AGENT;
	
	@NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin nominatedByAdmin;

    @NotNull
    @OneToMany(mappedBy = "estateAgent", cascade = CascadeType.ALL)
    private List<Ad> publishedAds = new ArrayList<>();
    
    /*
    public Offer makeOffer(User utente, Long adId) {
    	
    	Ad ad = getPublishedAd(adId);
    	Offer nuova = new Offer(20L, new BigDecimal(40), OfferState.PENDING, utente, ad);
    	
    	ad.offers.add(nuova);
    	
    	return nuova;
    }
    */
}
