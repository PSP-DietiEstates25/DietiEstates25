package com.dietiestates.api.model;

import jakarta.persistence.Entity;
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
public class SearchUser {
	
	@Id
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "user_email",
			foreignKey = @ForeignKey(name = "USER_EMAIL_FK"))
	private User user;

	@Id
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "search_id",
			foreignKey = @ForeignKey(name = "SEARCH_ID_FK"))
	private Search search;
	
}
