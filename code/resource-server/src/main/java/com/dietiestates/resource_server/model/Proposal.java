package com.dietiestates.resource_server.model;

import com.dietiestates.resource_server.enums.ProposalCategory;
import com.dietiestates.resource_server.enums.ProposalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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
			name = "negotiation_id",
			foreignKey = @ForeignKey(name = "PROPOSAL_NEGOTIATION_ID_FK"))
	private Negotiation negotiation;
	
	protected Proposal(
			String proposalCategory,
			String proposalStatus,
			Negotiation negotiation
			) {
		this.proposalCategory = ProposalCategory.valueOf(proposalCategory);
		this.proposalStatus = ProposalStatus.valueOf(proposalStatus);
		negotiation.addProposal(this);
	}

}
