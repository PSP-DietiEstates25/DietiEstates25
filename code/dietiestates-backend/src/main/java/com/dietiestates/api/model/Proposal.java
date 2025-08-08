package com.dietiestates.api.model;

import com.dietiestates.api.enums.ProposalCategory;
import com.dietiestates.api.enums.ProposalStatus;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "category")
public abstract class Proposal {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long proposalCode;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@Enumerated(EnumType.STRING)
	private ProposalCategory proposalCategory;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@Enumerated(EnumType.STRING)
	private ProposalStatus proposalStatus;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "user_email",
			foreignKey = @ForeignKey(name = "USER_EMAIL_FK"))
	private User user;
}
