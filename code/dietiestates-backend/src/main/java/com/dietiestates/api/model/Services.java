package com.dietiestates.api.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
public class Services {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private Boolean hasAirConditioning;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private Boolean hasDoorman;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private Boolean hasElevator;

	@NotNull
	@OneToOne(mappedBy = "services", cascade = CascadeType.ALL, orphanRemoval = true)
	private Detail detail;
	
	public void addDetail(Detail detail) {
		this.setDetail(detail);
		detail.setServices(this);
	}
}
