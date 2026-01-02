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
public class SquareMetersRange {

	@Column(nullable = false, name = "min_square_meters")
	private Integer minSquareMeters;
	
	@Column(nullable = false, name = "max_square_meters")
	private Integer maxSquareMeters;

    public boolean contains(Integer squareMeters) {
        return squareMeters != null
                && minSquareMeters != null
                && maxSquareMeters != null
                && minSquareMeters <= squareMeters
                && squareMeters <= maxSquareMeters;
    }
}
