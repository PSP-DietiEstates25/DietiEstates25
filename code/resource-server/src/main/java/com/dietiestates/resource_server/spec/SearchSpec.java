package com.dietiestates.resource_server.spec;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchSpec {

	private String category;
	private Long cadastralFilterId;
	private Long detailId;
}
