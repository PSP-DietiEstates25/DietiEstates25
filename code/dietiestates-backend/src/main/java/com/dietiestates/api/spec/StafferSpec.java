package com.dietiestates.api.spec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StafferSpec extends AuthenticationSpec {

	private String adminEmail;
	
	@Builder(builderMethodName = "stafferSpecBuilder")
	public StafferSpec(
			String email,
			String password,
			Boolean accountLocked,
			Boolean enabled,
			String role,
			String adminEmail
			) {
		super(email, password, accountLocked, enabled, role);
		this.adminEmail = adminEmail;
	}
}
