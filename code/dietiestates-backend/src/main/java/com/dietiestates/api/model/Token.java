package com.dietiestates.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String token;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime expiresAtDate;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime validatedAtDate;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "account_id",
			foreignKey = @ForeignKey(name = "TOKEN_DEFAULT_ACCOUNT_FK")
			)
	private DefaultAccount defaultAccount;
	
	@Builder(builderMethodName = "builder")
	public Token(
			String token,
			DefaultAccount defaultAccount
			) {
		this.token = token;
		defaultAccount.addToken(this);
	}
}
