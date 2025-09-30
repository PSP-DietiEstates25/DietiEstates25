package com.dietiestates.api.model;

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
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Search {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AdCategory category;
	
	@Column(nullable = false)
	private Integer size;
	
	@Column(nullable = false)
	private Integer page;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	
	@OneToOne(mappedBy = "search", cascade = CascadeType.ALL, orphanRemoval = true)
	private Detail detail;
	
	@OneToOne(mappedBy = "search", cascade = CascadeType.ALL, orphanRemoval = true)
	private CadastralFilter cadastralFilter;
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "user_email",
			foreignKey = @ForeignKey(name = "SEARCH_USER_EMAIL_FK"))
	private User user;
	
	@OneToMany(mappedBy = "search", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SearchRealEstate> searchRealEstates = new ArrayList<>();
	
	public void addSearchRealEstate(SearchRealEstate searchRealEstate) {
		this.searchRealEstates.add(searchRealEstate);
		searchRealEstate.setSearch(this);
	}
}
