package com.dietiestates.api.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class EstateAgent extends Staffer {
	
	@OneToMany(mappedBy = "estateAgent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RealEstate> realEstates = new ArrayList<>();
	
	@Builder(builderMethodName = "builder")
	public EstateAgent(
			DefaultAccount securityAccountDecorator,
			Admin admin
			) {
		super(securityAccountDecorator, admin);
	}
	
	public void addRealEstate(RealEstate realEstate) {
		realEstates.add(realEstate);
		realEstate.setEstateAgent(this);
	}
}
