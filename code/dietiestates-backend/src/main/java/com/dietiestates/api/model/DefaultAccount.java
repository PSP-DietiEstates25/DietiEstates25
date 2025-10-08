package com.dietiestates.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DefaultAccount implements Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToMany(mappedBy="defaultAccount", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Token> tokens = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "role_name",
			foreignKey = @ForeignKey(name = "DEFAULT_ACCOUNT_ROLE_FK")
			)
	private Role role;
	
	@Builder(builderMethodName = "builder")
	public DefaultAccount(
			String email,
			String password,
			Role role
			) {
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	@Override
	public Long getAccountId() {
		return id;
	}
	
	@Override
	public String getAccountPassword() {
		return password;
	}

	@Override
	public String getAccountEmail() {
		return email;
	}
	
	@Override
	public String getAccountRole() {
		return role.getName();
	}

	@Override
	public void addToken(Token token) {
		tokens.add(token);
		token.setDefaultAccount(this);
	}
}
