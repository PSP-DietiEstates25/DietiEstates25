package com.dietiestates.resource_server.spec;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSpec {

	private String message;
	private String userEmail;
}
