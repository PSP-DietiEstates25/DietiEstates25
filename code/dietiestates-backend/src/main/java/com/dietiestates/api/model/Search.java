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
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
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
	
	@OneToMany(mappedBy = "search", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SearchRealEstate> searchRealEstates = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(
			nullable = false,
			name = "user_email",
			foreignKey = @ForeignKey(name = "SEARCH_USER_EMAIL_FK"))
	private User user;
	
	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "detail_id",
			foreignKey = @ForeignKey(name = "SEARCH_DETAIL_ID_FK"))
	private Detail detail;
	
	@OneToOne
	@JoinColumn(
			nullable = false,
			name = "cadastral_filter_id",
			foreignKey = @ForeignKey(name = "SEARCH_CADASTRAL_FILTER_ID_FK"))
	private CadastralFilter cadastralFilter;
	
	@Builder(builderMethodName = "builder")
	public Search(
			String category,
			Integer size,
			Integer page,
			User user,
			CadastralFilter cadastralFilter,
			Detail detail
			) {
		this.category = AdCategory.valueOf(category);
		this.size = size;
		this.page = page;
		this.user = user;
		user.addSearch(this);
		setCadastralFilter(cadastralFilter);
		this.detail = detail;
	}
	
	public void addSearchRealEstate(SearchRealEstate searchRealEstate) {
		this.searchRealEstates.add(searchRealEstate);
		searchRealEstate.setSearch(this);
	}
	
	public void setCadastralFilter(CadastralFilter cadastralFilter) {
		this.cadastralFilter = cadastralFilter;
		cadastralFilter.setSearch(this);
	}
	
}
