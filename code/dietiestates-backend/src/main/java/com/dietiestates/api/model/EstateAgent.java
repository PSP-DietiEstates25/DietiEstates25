package com.dietiestates.api.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "estate_agent")
public class EstateAgent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	// Mantengo il collegamento all'admin 
	@ManyToOne
	@JoinColumn(name = "admin_email", foreignKey = @ForeignKey(name = "ADMIN_EMAIL_FK"))
	private Admin admin;

	@NotNull
	@OneToMany(mappedBy = "estateAgent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RealEstateAd> publishedAd = new ArrayList<>();

	public void addPublishedAd(RealEstateAd ad) {
		publishedAd.add(ad);
		ad.setEstateAgent(this);
	}
}
