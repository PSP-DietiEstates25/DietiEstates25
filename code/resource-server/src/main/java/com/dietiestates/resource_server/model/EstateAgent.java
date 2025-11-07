package com.dietiestates.resource_server.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class EstateAgent extends Staffer {
	
	@OneToMany(mappedBy = "estateAgent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RealEstate> realEstates = new ArrayList<>();

    @OneToMany(mappedBy = "estateAgent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Negotiation> negotiations = new ArrayList<>();
	
	@Builder(builderMethodName = "builder")
	public EstateAgent(String email, Admin admin) {
		super(email, admin);
	}
	
	public void addRealEstate(RealEstate realEstate) {
		realEstates.add(realEstate);
		realEstate.setEstateAgent(this);
	}

    public void addNegotiation(Negotiation negotiation){
        this.negotiations.add(negotiation);
        negotiation.setEstateAgent(this);
    }
}
