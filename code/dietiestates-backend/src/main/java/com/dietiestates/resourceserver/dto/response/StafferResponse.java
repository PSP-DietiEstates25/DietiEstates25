package com.dietiestates.resourceserver.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StafferResponse {

	private Long id;
	private String email;
	private String adminEmail;
	
	@Builder(builderMethodName = "builder")
	public StafferResponse(
			Long id,
			String email,
			String adminEmail
			) {
		this.id = id;
		this.email = email;
		this.adminEmail = adminEmail;
	}
}
