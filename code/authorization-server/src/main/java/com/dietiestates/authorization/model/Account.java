package com.dietiestates.authorization.model;

import java.time.LocalDateTime;

public interface Account {
	Long getAccountId();
	String getAccountEmail();
	String getAccountPassword();
	Role getAccountRole();
	LocalDateTime getCreatedDate();
	LocalDateTime getLastModifiedDate();
}