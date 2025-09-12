package com.dietiestates.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dietiestates.api.enums.AdCategory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Search {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AdCategory category;
	
	@Column(nullable = false)
	private BigDecimal minimumPrice;
	
	@Column(nullable = false)
	private BigDecimal maximumPrice;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToOne(mappedBy = "search", cascade = CascadeType.ALL, orphanRemoval = true)
	private Details details;
	
	@OneToMany(mappedBy = "search", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<SearchUser> users = new ArrayList<>();
	
	public void addUser(SearchUser user) {
		users.add(user);
		user.setSearch(this);
	}
	
	public void addDetails(Details details) {
		this.details = details;
		details.setSearch(this);
	}
}
