package com.dietiestates.resource_server.spec;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RealEstateSpec {
	private String category;
    private List<String> images;
	private String description;
	private Long cadastralDataId;
	private Long detailId;
}
