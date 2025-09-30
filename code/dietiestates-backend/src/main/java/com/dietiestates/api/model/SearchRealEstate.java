package com.dietiestates.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SearchRealEstate {

	@EmbeddedId
	private SearchRealEstateKey id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("realEstateId")
	@JoinColumn(name = "real_estate_id")
	private RealEstate realEstate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("searchId")
	@JoinColumn(name = "search_id")
	private Search search;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@Builder(builderMethodName = "searchRealEstateBuilder")
	public SearchRealEstate(
			LocalDateTime createdDate,
			RealEstate realEstate,
			Search search
			) {
		this.id = new SearchRealEstateKey(realEstate.getId(), search.getId());
		this.createdDate = createdDate;
		this.realEstate = realEstate;
		this.search = search;
	}
}
