package com.dietiestates.resourceserver.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AuthenticationResponse {

	private String token;
}
