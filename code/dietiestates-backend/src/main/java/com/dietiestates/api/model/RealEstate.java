package com.dietiestates.api.model;

import java.math.BigDecimal;

import com.dietiestates.api.enums.EnergyClass;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class RealEstate {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private BigDecimal price;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private Float size;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private String address;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private Integer rooms;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private Integer floor;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private EnergyClass energyClass;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private Double latitude;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private Double longitude;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@OneToOne(mappedBy = "realEstate", cascade = CascadeType.ALL, orphanRemoval = true)
	private Ad ad;
	
	public void addAd(Ad ad) {
		this.setAd(ad);
		ad.setRealEstate(this);
	}
}
