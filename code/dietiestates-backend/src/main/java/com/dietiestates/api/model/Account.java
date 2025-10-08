package com.dietiestates.api.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

public interface Account extends Authenticable {
	Long getAccountId();
	String getAccountEmail();
	String getAccountRole();
	LocalDateTime getCreatedDate();
	LocalDateTime getLastModifiedDate();
	List<Token> getTokens();
	void addToken(Token token);
}
