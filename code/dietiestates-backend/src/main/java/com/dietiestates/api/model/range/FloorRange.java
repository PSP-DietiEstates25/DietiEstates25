package com.dietiestates.api.model.range;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
public class FloorRange {

	@Column(nullable = false, name = "min_floor")
	private Integer minFloor;

	@Column(nullable = false, name = "max_floor")
	private Integer maxFloor;

	public Boolean contains(Integer floor) {
		if (floor == null)
			return false;
		if (this.minFloor != null && floor < this.minFloor)
			return false;
		if (this.maxFloor != null && floor > this.maxFloor)
			return false;
		return true;
	}

}
