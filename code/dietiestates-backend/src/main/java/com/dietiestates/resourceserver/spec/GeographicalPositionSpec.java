package com.dietiestates.resourceserver.spec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeographicalPositionSpec {

	private String city;
	private String municipality;
	private String address;
	private Double latitude;
	private Double longitude;
	private Integer radius;
}
