package com.dietiestates.resource_server.model.range;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EnergyClassRange {

	@Column(nullable = false, name = "min_energy_class")
	private Integer minEnergyClass;
	
	@Column(nullable = false, name = "max_energy_class")
	private Integer maxEnergyClass;
	
	public Boolean contains(Integer energyClass) {
		
		if (energyClass != null && this.minEnergyClass != null && this.maxEnergyClass != null) {
			
			if(this.minEnergyClass.compareTo(energyClass) <= 0 && energyClass.compareTo(this.maxEnergyClass) <= 0)
				return true;
			else return false;
			
		} else return false;
	}
}
