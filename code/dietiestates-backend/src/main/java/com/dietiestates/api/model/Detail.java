package com.dietiestates.api.model;

import java.time.LocalDateTime;

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
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Detail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToOne(mappedBy = "detail", cascade = CascadeType.ALL, orphanRemoval = true)
	private GeographicalPosition geographicalPosition;
	
	@OneToOne(mappedBy = "detail", cascade = CascadeType.ALL, orphanRemoval = true)
	private Utility utility;
		
	@OneToOne
	@JoinColumn(
			nullable = true,
			name = "search_id",
			foreignKey = @ForeignKey(name = "DETAIL_SEARCH_ID_FK"))
	private Search search;
	
	@OneToOne
    @JoinColumn(
			nullable = true,
			name = "real_estate_id",
			foreignKey = @ForeignKey(name = "DETAIL_REAL_ESTATE_ID_FK"))
	private RealEstate realEstate;
	
	@Builder(builderMethodName = "detailBuilder")
	public Detail(
			LocalDateTime createdDate,
			RealEstate realEstate,
			Search search
			){
		this.createdDate = createdDate;
		
		if(realEstate != null)
			this.setRealEstate(realEstate);
		
		if(search != null)
			this.setSearch(search);
	}
	
	public void setRealEstate(RealEstate realEstate) {
		this.realEstate = realEstate;
		realEstate.setDetail(this);
	}
	
	public void setSearch(Search search) {
		this.search = search;
		search.setDetail(this);;
	}
	
}
