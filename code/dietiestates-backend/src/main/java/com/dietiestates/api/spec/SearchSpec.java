package com.dietiestates.api.spec;

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
public class SearchSpec {

	private String category;
	private Integer size;
	private Integer page;
	private String userEmail;
	private Long detailId;
	private CadastralFilterSpec cadastralFilterSpec;	
}
