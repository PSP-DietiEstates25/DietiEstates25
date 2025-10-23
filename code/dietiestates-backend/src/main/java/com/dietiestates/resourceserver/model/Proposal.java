package com.dietiestates.resourceserver.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.dietiestates.resourceserver.enums.ProposalCategory;
import com.dietiestates.resourceserver.enums.ProposalStatus;

import jakarta.persistence.Column;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "category")
public abstract class Proposal {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProposalCategory proposalCategory;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProposalStatus proposalStatus;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;

	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;

	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "user_id",
			foreignKey = @ForeignKey(name = "PROPOSAL_USER_ID_FK"))
	private User user;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "real_estate_id",
			foreignKey = @ForeignKey(name = "PROPOSAL_REAL_ESTATE_ID"))
	private RealEstate realEstate;
	
	public Proposal(
			String proposalCategory,
			String proposalStatus,
			User user,
			RealEstate realEstate
			) {
		this.proposalCategory = ProposalCategory.valueOf(proposalCategory);
		this.proposalStatus = ProposalStatus.valueOf(proposalStatus);
		user.addProposal(this);
		realEstate.addProposal(this);
	}

}
