package com.dietiestates.api.model.range;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class EnergyClassRange {

	@Column(nullable = false, name = "min_energy_class")
	private Integer minEnergyClass;
	
	@Column(nullable = false, name = "max_energy_class")
	private Integer maxEnergyClass;
}
