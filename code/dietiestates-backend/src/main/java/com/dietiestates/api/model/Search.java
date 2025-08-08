package com.dietiestates.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dietiestates.api.enums.AdCategory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
public class Search {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@Enumerated(EnumType.STRING)
	private AdCategory adCategory;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private BigDecimal minimumPrice;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	private BigDecimal maximumPrice;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@ManyToOne
	@JoinColumn(
			name = "detail_id",
			foreignKey = @ForeignKey(name = "DETAIL_ID_FK"))
	private Detail detail;
	
	@EqualsAndHashCode.Exclude
	@NotNull
	@OneToMany(mappedBy = "search", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SearchUser> users = new ArrayList<>();
	
	public void addUser(SearchUser user) {
		users.add(user);
		user.setSearch(this);
	}
}
