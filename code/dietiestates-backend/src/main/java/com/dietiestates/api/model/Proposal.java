package com.dietiestates.api.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.enums.ProposalCategory;
import com.dietiestates.api.enums.ProposalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "category")
@EntityListeners(AuditingEntityListener.class)
public abstract class Proposal {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	protected Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	protected ProposalCategory proposalCategory;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	protected ProposalStatus proposalStatus;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "estete_agent_id",
			foreignKey = @ForeignKey(name = "PROPOSAL_ESTATE_AGENT_ID_FK"))
	protected EstateAgent estateAgent;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "user_id",
			foreignKey = @ForeignKey(name = "PROPOSAL_USER_ID_FK"))
	protected User user;
}
