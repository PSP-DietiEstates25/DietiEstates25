package com.dietiestates.resource_server.spec;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RealEstateSpec {

	private String category;
	private String[] images;
	private String description;
	private String estateAgentEmail;
	private Long cadastralDataId;
	private Long detailId;
}
