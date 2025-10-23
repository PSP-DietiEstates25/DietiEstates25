package com.dietiestates.authorization.spec;

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
public class AccountSpec {

	private String email;
	private String password;
	private Boolean enabled;
	private Boolean locked;
}