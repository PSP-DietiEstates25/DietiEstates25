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
public class NotificationSpec {

	private String message;
	//private String notificationCategoryName;
	private String userEmail;
}
