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
	
	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "search_id",
			foreignKey = @ForeignKey(name = "CADASTRAL_FILTER_SEARCH_ID_FK"))
	private Search search;
	
	public void setSearch(Search search) {
		this.search = search;
		search.setCadastralFilter(this);;
	}
}
