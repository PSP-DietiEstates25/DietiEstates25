package com.dietiestates.resource_server.spec;

import lombok.*;

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
