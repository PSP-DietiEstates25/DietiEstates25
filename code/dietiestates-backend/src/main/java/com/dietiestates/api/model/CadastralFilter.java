package com.dietiestates.api.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.model.range.FloorRange;
import com.dietiestates.api.model.range.PriceRange;
import com.dietiestates.api.model.range.RoomsRange;
import com.dietiestates.api.model.range.SquareMetersRange;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CadastralFilter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Embedded
	private PriceRange priceRange;
	
	@Embedded
	private SquareMetersRange squareMetersRange;
	
	//@Embedded
	//private IntegerRange energyClassRange;
	
	@Embedded
	private RoomsRange roomsRange;
	
	@Embedded
	private FloorRange floorRange;
	
	@OneToOne(mappedBy = "cadastralFilter", cascade = CascadeType.ALL, orphanRemoval = true)
	private Search search;
}
