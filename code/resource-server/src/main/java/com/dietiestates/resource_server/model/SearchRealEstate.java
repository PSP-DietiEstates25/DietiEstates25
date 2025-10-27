package com.dietiestates.resource_server.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SearchRealEstate {

	@EmbeddedId
	private SearchRealEstateKey id;
	
	@ManyToOne
	@MapsId("realEstateId")
	@JoinColumn(name = "real_estate_id")
	private RealEstate realEstate;
	
	@ManyToOne
	@MapsId("searchId")
	@JoinColumn(name = "search_id")
	private Search search;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@Builder(builderMethodName = "builder")
	public SearchRealEstate(
			RealEstate realEstate,
			Search search
			) {
		this.id = new SearchRealEstateKey(realEstate.getId(), search.getId());
		realEstate.addSearchRealEstate(this);
		search.addSearchRealEstate(this);
	}
	
	public void setParentAssociations() {
		realEstate.addSearchRealEstate(this);
		search.addSearchRealEstate(this);
	}
}
