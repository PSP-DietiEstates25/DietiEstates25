package com.dietiestates.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StafferResponse {

	private Long id;
	private AccountResponse account;
	private String adminEmail;
	
	@Builder(builderMethodName = "builder")
	public StafferResponse(
			Long id,
			AccountResponse account,
			String adminEmail
			) {
		this.id = id;
		this.account = account;
		this.adminEmail = adminEmail;
	}
}
