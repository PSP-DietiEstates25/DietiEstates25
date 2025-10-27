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
	private Integer size;
	private Integer page;
	private String userEmail;
	private Long cadastralFilterId;
	private Long detailId;
}
