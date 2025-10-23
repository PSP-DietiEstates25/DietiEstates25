package com.dietiestates.resourceserver.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AccountResponse {

	private Long id;
	private String email;
	private String role;
	private Boolean enabled;
	private Boolean locked;
}
