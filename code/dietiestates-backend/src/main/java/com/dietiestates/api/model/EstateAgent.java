package com.dietiestates.api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DiscriminatorValue(value = "estate_agent")
public class EstateAgent extends Staffer {

	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "admin_email",
			foreignKey = @ForeignKey(name = "ADMIN_EMAIL_FK"))
	private Admin admin;
	
	@NotNull
	@OneToMany(mappedBy = "estateAgent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RealEstateAd> publishedAd = new ArrayList<>();
	
	public void addPublishedAd(RealEstateAd ad) {
		publishedAd.add(ad);
		ad.setEstateAgent(this);
	}
}
