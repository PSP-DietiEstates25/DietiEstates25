package com.dietiestates.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SearchRequest {
	
	@NotEmpty(message = "Category is mandatory")
	@NotBlank(message = "Category is mandatory")
	private String category;
	
	@NotNull(message = "Size is mandatory")
	@Positive(message = "Size must be a positive number")
	private Integer size;
	
	@NotNull(message = "Page is mandatory")
	@Positive(message = "Page must be a positive number")
	private Integer page;
	
	@NotEmpty(message = "User email is mandatory")
	@NotBlank(message = "User email is mandatory")
	@Email
	private String userEmail;
	
	@Positive(message = "Detail id must be a positive number")
	private Long detailId;
	
	@NotNull(message = "Cadastral filter is mandatory")
	@Valid
	private CadastralFilterRequest cadastralFilter;
	
}
