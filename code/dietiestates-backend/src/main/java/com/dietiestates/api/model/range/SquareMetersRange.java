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
public class SquareMetersRange {

	@Column(nullable = false, name = "min_square_meters")
	private Integer minSquareMeters;

	@Column(nullable = false, name = "max_square_meters")
	private Integer maxSquareMeters;

	public Boolean contains(Integer sqm) {
		if (sqm == null)
			return false;
		if (this.minSquareMeters != null && sqm < this.minSquareMeters)
			return false;
		if (this.maxSquareMeters != null && sqm > this.maxSquareMeters)
			return false;
		return true;
	}

}
