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
public class FloorRange {
	
	@Column(nullable = false, name = "min_floor")
	private Integer minFloor;

	@Column(nullable = false, name = "max_floor")
	private Integer maxFloor;

    public boolean contains(Integer floor) {
        return floor != null
                && minFloor != null
                && maxFloor != null
                && minFloor <= floor
                && floor <= maxFloor;
    }
}
