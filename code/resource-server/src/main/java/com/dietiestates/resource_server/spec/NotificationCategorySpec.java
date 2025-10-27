package com.dietiestates.resource_server.spec;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCategorySpec {

	private String name;
	private Boolean isActive;
}
