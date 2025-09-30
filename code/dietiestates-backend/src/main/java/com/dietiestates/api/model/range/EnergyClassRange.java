package com.dietiestates.api.model.range;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode
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
