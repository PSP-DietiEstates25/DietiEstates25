package com.dietiestates.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.enums.ProposalCategory;
import com.dietiestates.api.enums.ProposalStatus;

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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	protected ProposalCategory proposalCategory;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	protected ProposalStatus proposalStatus;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	protected LocalDateTime createdDate;

	@LastModifiedDate
	@Column(insertable = false)
	protected LocalDateTime lastModifiedDate;
	
	public Proposal(
			Long id,
			String proposalCategory,
			String proposalStatus,
			LocalDateTime createdDate,
			LocalDateTime lastModifiedDate
			) {
		this.id = id;
		this.proposalCategory = ProposalCategory.valueOf(proposalCategory);
		this.proposalStatus = ProposalStatus.valueOf(proposalStatus);
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
	}
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "user_id",
			foreignKey = @ForeignKey(name = "PROPOSAL_USER_ID_FK"))
	protected User user;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "real_estate_id",
			foreignKey = @ForeignKey(name = "PROPOSAL_REAL_ESTATE_ID"))
	protected RealEstate realEstate;
}
