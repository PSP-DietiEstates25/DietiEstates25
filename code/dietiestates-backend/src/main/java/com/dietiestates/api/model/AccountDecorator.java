package com.dietiestates.api.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public abstract class AccountDecorator implements Account {

	private DefaultAccount defaultAccount;
	
	public AccountDecorator(
			DefaultAccount defaultAccount
			) {
		this.defaultAccount = defaultAccount;
	}

	@Override
	public Long getAccountId() {
		return defaultAccount.getAccountId();
	}
	
	@Override
	public String getAccountEmail() {
		return defaultAccount.getAccountEmail();
	}
	
	@Override
	public String getAccountPassword() {
		return defaultAccount.getAccountPassword();
	}

	@Override
	public Role getAccountRole() {
		return defaultAccount.getAccountRole();
	}
	
	@Override
	public LocalDateTime getCreatedDate() {
		return defaultAccount.getCreatedDate();
	}
	
	@Override
	public LocalDateTime getLastModifiedDate() {
		return defaultAccount.getLastModifiedDate();
	}
	
	@Override
	public List<Token> getTokens(){
		return defaultAccount.getTokens();
	}
	
	@Override
	public void addToken(Token token) {
		defaultAccount.addToken(token);
	}
	
}
