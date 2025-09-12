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
public class Detail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private long id;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "geographical_position_id",
			foreignKey = @ForeignKey(name = "DETAIL_GEOGRAPHICAL_POSITION_ID_FK"))
	private GeographicalPosition geographicalPosition;
	
	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "utility_id",
			foreignKey = @ForeignKey(name = "DETAIL_UTILITY_ID_FK"))
	private Utility utility;
	
	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "cadastral_data_id",
			foreignKey = @ForeignKey(name = "DETAIL_CADASTRAL_DATA_ID_FK"))
	private CadastralData cadastralData;
		
	@OneToOne(mappedBy = "detail", cascade = CascadeType.ALL, orphanRemoval = true)
	private Search search;
	
	@OneToOne(mappedBy = "detail", cascade = CascadeType.ALL, orphanRemoval = true)
	private RealEstate realEstate;
	
	@Builder(builderMethodName = "detailBuilder")
	public Detail(
			LocalDateTime createdDate,
			GeographicalPosition geographicalPosition,
			Utility utility,
			CadastralData cadastralData
			){
		this.createdDate = createdDate;
		this.geographicalPosition = geographicalPosition;
		this.utility = utility;
		this.cadastralData = cadastralData;
		geographicalPosition.addDetail(this);
		utility.addDetail(this);
		cadastralData.addDetail(this);
	}
	
	public void addRealEstate(RealEstate realEstate) {
		this.realEstate = realEstate;
		realEstate.setDetail(this);
	}
	
	public void addSearch(Search search) {
		this.search = search;
		search.setDetail(this);;
	}
	
}
