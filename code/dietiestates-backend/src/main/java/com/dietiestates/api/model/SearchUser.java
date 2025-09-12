package com.dietiestates.api.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SearchUser {
	
	@Id
	@NotNull
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "user_id",
			foreignKey = @ForeignKey(name = "SEARCH_USER_USER_ID_FK"))
	private User user;

	@Id
	@NotNull
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "search_id",
			foreignKey = @ForeignKey(name = "SEARCH_USER_SEARCH_ID_FK"))
	private Search search;
	
}
