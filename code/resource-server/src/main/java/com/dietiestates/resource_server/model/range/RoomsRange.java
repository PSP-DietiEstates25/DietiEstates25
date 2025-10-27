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
public class RoomsRange {

	@Column(nullable = false, name = "min_rooms")
	private Integer minRooms;
	
	@Column(nullable = false, name = "max_rooms")
	private Integer maxRooms;

	public Boolean contains(Integer rooms) {
		
		if(rooms != null && this.minRooms != null && this.maxRooms != null) {
			
			if(this.minRooms.compareTo(rooms) <= 0 && rooms.compareTo(this.maxRooms) <= 0)
				return true;
			else return false;
			
		} else return false;
	}
}
