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

    public boolean contains(Integer energyClass) {
        return energyClass != null
                && minEnergyClass != null
                && maxEnergyClass != null
                && minEnergyClass <= energyClass
                && energyClass <= maxEnergyClass;
    }
}
