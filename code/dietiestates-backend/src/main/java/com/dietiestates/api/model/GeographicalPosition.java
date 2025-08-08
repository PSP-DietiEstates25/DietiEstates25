package com.dietiestates.api.model;

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
public class GeographicalPosition {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private String city;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private String municipality;
	
	@EqualsAndHashCode.Exclude
	private Double zoneMarkerLatitude;
	
	@EqualsAndHashCode.Exclude
	private Double zoneMarkerLongitude;
	
	@EqualsAndHashCode.Exclude
	private Float zoneMarkerRadius;
	
	@NotNull
	@OneToOne(mappedBy = "geographicalPosition", cascade = CascadeType.ALL, orphanRemoval = true)
	private Detail detail;
	
	public void addDetail(Detail detail) {
		this.setDetail(detail);
		detail.setGeographicalPosition(this);
	}
}
