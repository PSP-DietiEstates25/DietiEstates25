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
public class SquareMetersRange {

	@Column(nullable = false, name = "min_square_meters")
	private Integer minSquareMeters;
	
	@Column(nullable = false, name = "max_square_meters")
	private Integer maxSquareMeters;
	
	public Boolean contains(Integer squareMeters) {
		
		if(squareMeters != null && this.minSquareMeters != null && this.maxSquareMeters != null) {
			
			if(this.minSquareMeters.compareTo(squareMeters) < 0 && squareMeters.compareTo(this.maxSquareMeters) <= 0)
				return true;
			else return false;
			
		} else return false;
	}
}
