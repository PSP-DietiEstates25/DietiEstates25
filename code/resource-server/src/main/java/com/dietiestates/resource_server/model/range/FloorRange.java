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
//@EqualsAndHashCode
@Embeddable
public class FloorRange {
	
	@Column(nullable = false, name = "min_floor")
	private Integer minFloor;

	@Column(nullable = false, name = "max_floor")
	private Integer maxFloor;

	public Boolean contains(Integer floor) {
		
		if (floor != null && this.minFloor != null && this.maxFloor != null) {
			
			if(this.minFloor.compareTo(floor) <= 0 && floor.compareTo(this.maxFloor) <= 0)
				return true;
			else return false;
			
		} else return false;
	}
}
