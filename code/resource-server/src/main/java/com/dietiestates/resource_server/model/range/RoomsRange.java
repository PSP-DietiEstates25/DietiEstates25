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
public class RoomsRange {

	@Column(nullable = false, name = "min_rooms")
	private Integer minRooms;
	
	@Column(nullable = false, name = "max_rooms")
	private Integer maxRooms;

    public boolean contains(Integer rooms) {
        return rooms != null
                && minRooms != null
                && maxRooms != null
                && minRooms <= rooms
                && rooms <= maxRooms;
    }
}
