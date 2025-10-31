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
// @EqualsAndHashCode
@Embeddable
public class RoomsRange {

	@Column(nullable = false, name = "min_rooms")
	private Integer minRooms;

	@Column(nullable = false, name = "max_rooms")
	private Integer maxRooms;

	public Boolean contains(Integer rooms) {
		if (rooms == null)
			return false;
		if (this.minRooms != null && rooms < this.minRooms)
			return false;
		if (this.maxRooms != null && rooms > this.maxRooms)
			return false;
		return true;
	}

}
