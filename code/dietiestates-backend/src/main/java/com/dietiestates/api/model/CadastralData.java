package com.dietiestates.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.dto.CadastralDataDto;
import com.dietiestates.api.enums.EnergyClass;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class CadastralData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer squareMeters;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnergyClass energyClass;

    @Column(nullable = false)
    private Integer rooms;

    @Column(nullable = false)
    private Integer floor;
    
    @CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "real_estate_id",
			foreignKey = @ForeignKey(name = "CADASTRAL_DATA_REAL_ESTATE_ID_FK"))
	private RealEstate realEstate;
	
	@Builder(builderMethodName = "cadastralDataBuilder")
	public CadastralData(
		LocalDateTime createdDate,
		BigDecimal price,
		Integer squareMeters,
		String energyClass,
		Integer rooms,
		Integer floor,
		RealEstate realEstate
		) {
		this.createdDate = createdDate;
		this.price = price;
		this.squareMeters = squareMeters;
		this.energyClass = EnergyClass.valueOf(energyClass);
		this.rooms = rooms;
		this.floor = floor;
		this.setRealEstate(realEstate);
	}
	
	public void setRealEstate(RealEstate realEstate) {
		this.realEstate = realEstate;
		realEstate.setCadastralData(this);
	}
}
